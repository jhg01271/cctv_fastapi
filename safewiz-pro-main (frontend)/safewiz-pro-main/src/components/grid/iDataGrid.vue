<template>
    <div ref="gridEl" id="placeholder-grid-id"></div>
</template>
<script setup>
/**
 * ============================================================================================================================================================
 * 👉 [공통컴포넌트] Data Grid(Tui-grid)
 * ->          columns : array
 * ->          data : array
 * ->          @rowClick : 클릭 했을 때
 * ->          @rowChange : 변경 됐을 때
 * ->          @rowView : 보기 버튼 클릭 했을 때
 * ->          @rowDelete : 삭제 버튼 클릭 했을 때
 * ============================================================================================================================================================
 * 작성자 : ESG 사업부 임현섭
 * 작성일 : 2024-05-27
 * ============================================================================================================================================================
 */

import { ref, onMounted, defineProps, defineEmits, defineExpose, watch } from 'vue';
import Grid from 'tui-grid';
import eventBus from '@/components/grid/eventBus';

import TuiGridFunctionButtonRenderer from '@/components/grid/TuiGridFunctionButtonRenderer'; // 클래스 파일을 import 합니다.
import TuiGridSelectCellRenderer from '@/components/grid/TuiGridSelectCellRenderer'; // 클래스 파일을 import 합니다.
import TuiGridSelectEditCellRenderer from '@/components/grid/TuiGridSelectEditCellRenderer'; // 클래스 파일을 import 합니다.
import TuiGridCheckCellRenderer from '@/components/grid/TuiGridCheckCellRenderer';

import { useSystemCodeStore } from '@/stores/system/setting/systemCode';
const systemCodeStore = useSystemCodeStore();

// import { useCompCodeStore } from '@/stores/comp/master/compCode';
// const compCodeStore = useCompCodeStore();

const props = defineProps({
    columns: Array,
    data: Array,
    options: Object,
    gridId: String // 고유한 ID를 props로 받음
});
const emit = defineEmits(['rowClick', 'rowChanged', 'rowView', 'rowDelete', 'rowEdit', 'rowSave', 'rowCancel', 'rowSort', 'check', 'uncheck', 'focusChange', 'rowDetail']);

const gridEl = ref();
const tuiGrid = ref();

let originData = ref();
let isSearch = ref(false); // 체크박스 동작 못하도록

// 체크박스 동작 관련 코드 추가 (isSearch, setCheckItems) - 조동하
// 백그라운드 컬러 초기화 관련 코드 추가 (removeHighlight, removeModified) - 조동하
const resetData = list => {
    isSearch.value = true;

    originData.value = list;
    tuiGrid.value.resetData(list);

    removeHighlight(tuiGrid.value);
    removeModified(tuiGrid.value);
    if (props.options && 'treeColumnOptions' in props.options) setCheckItems(list);

    isSearch.value = false;
};

import { useI18n } from 'vue-i18n';
const { locale, t } = useI18n();
watch(locale, async () => {
    getGridColumns().then(columns => {
        tuiGrid.value.setColumns(columns);
    });
});

const getGridColumns = async () => {
    const gridColumns = [];
    for (const column of props.columns) {
        column.header = t(column.column);
        if (column.renderer && column.renderer.type === 'functionButton') {
            gridColumns.push({
                ...column,
                renderer: {
                    type: TuiGridFunctionButtonRenderer,
                    button: column.renderer.button
                }
            });
        } else if (column.renderer && column.renderer.type === 'selectCell') {
            let responses;
            // if (column.renderer.scope === 'compCode') {
            //     responses = await Promise.all([compCodeStore.getCode(column.renderer.code)]);
            // } else
            // {
            responses = await Promise.all([systemCodeStore.getCode(column.renderer.code)]);
            //}

            let col;
            if (!column.editor) {
                col = {
                    ...column,
                    renderer: {
                        type: TuiGridSelectCellRenderer,
                        options: {
                            codeValue: 'code',
                            codeName: 'codeNm'
                        },
                        data: responses[0]
                    }
                };
            } else {
                col = {
                    ...column,
                    renderer: {
                        type: TuiGridSelectCellRenderer,
                        options: {
                            codeValue: 'code',
                            codeName: 'codeNm'
                        },
                        // options: column.renderer.options,
                        data: responses[0]
                    },
                    editor: {
                        type: TuiGridSelectEditCellRenderer,
                        options: {
                            codeValue: 'code',
                            codeName: 'codeNm'
                        },
                        data: responses[0]
                    }
                };
            }
            gridColumns.push(col);
        } else if (column.renderer && column.renderer.type === 'checkbox') {
            let col = {
                ...column,
                renderer: {
                    type: TuiGridCheckCellRenderer
                },
                // 체크박스 disable
                editor: {
                    type: Boolean,
                    options: {
                        editor: column.editor
                    }
                }
            };
            gridColumns.push(col);
        } else {
            gridColumns.push(column);
        }
    } //end for
    return gridColumns;
};
onMounted(async () => {
    const element = document.getElementById('placeholder-grid-id');
    if (element) {
        element.id = props.gridId;
    }
    let gridColumns = await getGridColumns();
    let defGridOptions = {
        el: gridEl.value,
        columns: gridColumns,
        scrollX: false,
        scrollY: false,
        header: { height: 44 },
        minBodyHeight: 44,
        // pageOptions: {
        //     perPage: 5
        // },
        rowHeaders: [],
        data: [],
        columnOptions: { resizable: true, minWidth: 50 }
    };
    const finalGridOptions = Object.assign({}, defGridOptions, props.options);
    tuiGrid.value = new Grid(finalGridOptions);
    // 윈도우 사이즈 변경 감지하여 그리드 다시 렌더링
    window.addEventListener('resize', handleResize);

    //이벤트 정의
    tuiGrid.value.on('click', async ev => {
        emit('rowClick', ev);
    });

    tuiGrid.value.on('focusChange', async ev => {
        const { rowKey, instance } = ev;
        await emit('focusChange', ev);
        highlightRow(instance, rowKey);
    });

    tuiGrid.value.on('check', async ev => {
        if (props.options && 'treeColumnOptions' in props.options && !isSearch.value) handleCheckGrid(ev, true);
        emit('check', ev);
    });

    tuiGrid.value.on('uncheck', async ev => {
        if (props.options && 'treeColumnOptions' in props.options) handleCheckGrid(ev, false);
        emit('uncheck', ev);
    });

    tuiGrid.value.on('editingStart', async ev => {
        updateIcons(ev.instance, ev.rowKey, ['save', 'cancel']);
    });

    tuiGrid.value.on('editingFinish', async ev => {
        emit('editingFinish', ev);
    });

    tuiGrid.value.on('afterChange', async ev => {
        ev.instance.setValue(ev.rowKey, ev.columnName, ev.value, false);
        const result = ev.changes.reduce((acc, { columnName, value }) => {
            acc[columnName] = value;
            return acc;
        }, {});

        emit('rowChanged', result);
    });

    tuiGrid.value.on('beforeSort', async ev => {
        // ev.s
        emit('rowSort', ev); //FIXME 그리드 함수로 정렬 구현이 어렵다. 외부 커스텀 아이콘으로 정렬을 해야 한다.
    });

    eventBus.on('viewAction', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('rowView', rowKey);
        }
    });

    eventBus.on('deleteAction', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('rowDelete', rowKey);
        }
    });

    eventBus.on('saveAction', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('rowSave', rowKey);
        }
    });

    eventBus.on('cancelAction', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('rowCancel', rowKey);
        }
    });

    eventBus.on('beforeSort', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('beforeSort', rowKey);
        }
    });

    eventBus.on('editAction', (rowKey, gridId) => {
        if (gridId === props.gridId) {
            emit('rowEdit', rowKey);
        }
    });
}); //end Mount

function handleResize(ev) {
    // console.log(22323);
    // tuiGrid.value.destroy();
    // tuiGrid.value.resetData([]);
}
/**
 * ============================================================================================================================================================
 * 그리드 포커스 로우 백그라운드 컬러 변경
 * ============================================================================================================================================================
 * 작성자 : ESG 사업부 조동하
 * 작성일 : 2024-07-29
 * ============================================================================================================================================================
 */
const highlightRow = (grid, rowKey) => {
    removeHighlight(grid);
    // 선택된 행에 highlight-row 클래스 추가
    const cells = document.querySelectorAll(`#${grid.el.id} td[data-row-key="${rowKey}"]`);
    if (cells) {
        cells.forEach(cell => {
            cell.classList.add('highlight-row');
        });
    }
};

const removeHighlight = grid => {
    // 모든 행에서 highlight-row 클래스를 제거
    const rows = document.querySelectorAll(`#${grid.el.id} td`);
    if (rows) {
        rows.forEach(row => {
            row.classList.remove('highlight-row');
        });
    }
};

const removeModified = grid => {
    // 모든 행에서 highlight-row 클래스를 제거
    const rows = document.querySelectorAll(`#${grid.el.id} td`);
    if (rows) {
        rows.forEach(row => {
            row.classList.remove('modified-row');
        });
    }
};

/**
 * ============================================================================================================================================================
 * 트리 체크박스 관련 메서드 시작
 * ============================================================================================================================================================
 * 작성자 : ESG 사업부 조동하
 * 작성일 : 2024-07-15
 * ============================================================================================================================================================
 */
const setCheckItems = list => {
    list.forEach(element => {
        if (element.cmd === 'U') {
            tuiGrid.value.check(element.rowKey);
        }
        if (element._children) {
            setCheckItems(element._children);
        }
    });
};

let isProgress = ref(false); // 순환 방지 플래그
let isManualUncheck = ref(false); // 수동 uncheck 구분  플래그

// flag >> true: check, false: uncheck
const handleCheckGrid = (ev, flag) => {
    updatedRow(ev, flag);
    if (!isSearch.value && isProgress.value) return;
    isProgress.value = true;

    if (!isManualUncheck.value) {
        // 자식 노드 상태 변경
        handleCheckStateRecursive(ev.rowKey, flag);
        // 부모 노드 상태 변경
        handleCheckStateParent(ev.rowKey, flag);
    }
    isProgress.value = false;
};

const handleCheckStateRecursive = (rowKey, flag) => {
    const children = tuiGrid.value.getChildRows(rowKey);
    // if (isChildren.value) return;
    children.forEach(child => {
        if (flag) {
            tuiGrid.value.check(child.rowKey);
        } else {
            tuiGrid.value.uncheck(child.rowKey);
        }
        handleCheckStateRecursive(child.rowKey, flag); // 자식의 자식들도 재귀적으로 uncheck 처리
    });
};

const handleCheckStateParent = (key, flag) => {
    const parent = tuiGrid.value.getParentRow(key);
    if (parent) {
        const children = tuiGrid.value.getChildRows(parent.rowKey);

        isManualUncheck.value = true;
        if (flag) {
            tuiGrid.value.check(parent.rowKey);
        } else {
            // 모든 자식 노드가 uncheck 상태인지 확인
            const allUnchecked = children.every(child => child._attributes.checked === flag);
            if (allUnchecked) {
                tuiGrid.value.uncheck(parent.rowKey);
            }
        }

        isManualUncheck.value = false;
        // 재귀적으로 부모 노드도 uncheck 상태로 변경
        handleCheckStateParent(parent.rowKey, flag);
    }
};
// 2024.07.29 조동하
// 체크 여부 수정될때 백그라운드 색상 변경
const updatedRow = (ev, flag) => {
    const { instance, rowKey } = ev;
    const row = instance.getRow(rowKey);
    const cells = document.querySelectorAll(`#${instance.el.id} table td[data-row-key="${rowKey}"]`);

    // flag true > check / false > uncheck
    if (flag) {
        if (row.cmd === 'I') modifiedRow(cells);
        else if (row.cmd === 'U') initializedRow(cells);
    } else {
        if (row.cmd === 'I') initializedRow(cells);
        else if (row.cmd === 'U') modifiedRow(cells);
    }
};

const modifiedRow = cells => {
    if (cells) {
        cells.forEach(cell => {
            // 선택된 행에 modified-row 클래스 추가
            cell.classList.add('modified-row');
        });
    }
};
const initializedRow = cells => {
    if (cells) {
        cells.forEach(cell => {
            // 선택된 행에 modified-row 클래스 제거
            cell.classList.remove('modified-row');
        });
    }
};
/**
 * ============================================================================================================================================================
 * 트리 체크박스 관련 메서드 종료
 * ============================================================================================================================================================
 */

// // 보기 버튼 클릭 이벤트 핸들러
function onViewButtonClick(rowKey) {
    eventBus.emit('viewAction', rowKey, props.gridId);
}
function onDeleteButtonClick(rowKey) {
    eventBus.emit('deleteAction', rowKey, props.gridId);
}
function onSaveButtonClick(rowKey) {
    eventBus.emit('saveAction', rowKey, props.gridId);
}
function onCancelButtonClick(rowKey) {
    eventBus.emit('cancelAction', rowKey, props.gridId);
}
function onEditButtonClick(rowKey) {
    eventBus.emit('editAction', rowKey, props.gridId);
}

function createButton(spanText, buttonClassName, clickHandler) {
    const el = document.createElement('button');
    el.type = 'button';
    el.classList = buttonClassName;

    const elSpan = document.createElement('span');
    elSpan.textContent = spanText;

    el.addEventListener('click', clickHandler);

    el.appendChild(elSpan);
    return el;
}

function updateIcons(grid, rowKey, buttonArray) {
    const cell = document.querySelector(`#${grid.el.id} td[data-row-key="${rowKey}"][data-column-name="func"]`);
    if (cell) {
        const iconContainer = cell.querySelector('div');
        iconContainer.innerHTML = ''; // 초기화

        if (buttonArray.length > 0) {
            buttonArray.forEach(item => {
                if (item === 'view') {
                    iconContainer.appendChild(createButton('보기', 'btn us radius info', () => onViewButtonClick(rowKey))); // 저장 버튼을 생성합니다.
                } else if (item === 'delete') {
                    iconContainer.appendChild(createButton('삭제', 'btn us radius warning', () => onDeleteButtonClick(rowKey))); // 취소 버튼을 생성합니다.
                } else if (item === 'save') {
                    iconContainer.appendChild(createButton('저장', 'btn us radius success', () => onSaveButtonClick(rowKey))); // 저장 버튼을 생성합니다.
                } else if (item === 'cancel') {
                    iconContainer.appendChild(createButton('취소', 'btn us radius normal', () => onCancelButtonClick(rowKey))); // 취소 버튼을 생성합니다.
                } else if (item === 'edit') {
                    iconContainer.appendChild(createButton('수정', 'btn us radius info', () => onEditButtonClick(rowKey))); // 취소 버튼을 생성합니다.
                } else if (item === 'detail') {
                    iconContainer.appendChild(createButton('상세보기', 'btn us radius info', () => onEditButtonClick(rowKey))); // 취소 버튼을 생성합니다.
                }
            });
        }
    }
}

defineExpose({
    tuiGrid,
    updateIcons,
    resetData,
    originData
});
</script>
<style lang="scss">
.gridSelect {
    width: 100%;
    height: 28px;
    margin: 5px 0;
    border: 1px solid #ccc;
}
.highlight-row {
    background-color: #ebebeb !important;
}
.modified-row {
    background-color: #fdf5da !important;
}
</style>
