import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
export const useMenuByGroupStore = defineStore('menuByGroup', () => {
    const gridGroupColumns = ref([
        {
            column: 'CMD',
            name: 'cmd',
            align: 'center',
            hidden: true
        },
        {
            column: '상세보기',
            name: 'func',
            align: 'center',
            width: 100,
            renderer: {
                type: 'functionButton',
                button: ['detail']
            }
        },
        {
            column: '그룹명',
            name: 'grpNm',
            align: 'center',
            filter: 'text', // 필터링 기능 활성화
            flex: 1
        },
        {
            column: '인원',
            name: 'count',
            width: 60,
            align: 'center',
            filter: 'number' // 필터링 기능 활성화
        },
        {
            column: '순번',
            name: 'grpOdr',
            width: 60,
            align: 'center',
            filter: 'number' // 필터링 기능 활성화
        },
        {
            column: '사용',
            name: 'useYn',
            width: 60,
            align: 'center',
            filter: 'text' // 필터링 기능 활성화
        }
    ]);
    const gridMemberColumns = ref([
        {
            column: 'CMD',
            name: 'cmd',
            align: 'center',
            hidden: true
        },
        {
            column: '이름',
            name: 'hrNm',
            align: 'center',
            filter: 'text', // 필터링 기능 활성화
            width: 100
        },
        {
            column: '직위',
            name: 'jbrpNm',
            align: 'center',
            filter: 'text', // 필터링 기능 활성화
            width: 100
        },
        {
            column: '전화번호',
            name: 'phone',
            align: 'center',
            filter: 'text', // 필터링 기능 활성화
            flex: 1
        }
    ]);
    const gridGroupMenuColumns = ref([
        {
            column: 'CMD',
            name: 'cmd',
            align: 'center',
            hidden: true
        },
        {
            column: '메뉴 순번',
            name: 'menuSeq',
            align: 'center',
            hidden: true
        },
        {
            column: '권한 순번',
            name: 'grpId',
            align: 'center',
            hidden: true
        },
        {
            column: '메뉴 아이디',
            name: 'menuId',
            align: 'center',
            minwidth: 100,
            filter: 'text' // 필터링 기능 활성화
        },
        {
            column: '메뉴명',
            name: 'menuNm',
            align: 'left',
            filter: 'text' // 필터링 기능 활성화
        },
        {
            column: '저장',
            name: 'savAh',
            align: 'center',
            width: 40,
            renderer: {
                type: 'checkbox'
            }
        },
        {
            column: '삭제',
            name: 'delAh',
            align: 'center',
            width: 40,
            renderer: {
                type: 'checkbox'
            }
        },
        {
            column: '출력',
            name: 'exlAh',
            align: 'center',
            width: 60,
            renderer: {
                type: 'checkbox'
            }
        }
    ]);

    const menuTree = ref([]);
    const setMenuTree = list => {
        menuTree.value = list;
    };


    const useMenuTree = computed(() => {
        return filterTreeUse(menuTree.value);
    });

    const unUseMenuTree = computed(() => {
        return filterTreeUnUse(menuTree.value);
    });

    const flattenTree = computed(() => {
        let result = [];

        function recursiveFlatten(node) {
            result.push(node);

            if (node._children && node._children.length > 0) {
                node._children.forEach(child => recursiveFlatten(child));
            }
        }

        useMenuTree.value.forEach(node => recursiveFlatten(node));

        return result;
    });

    function filterTreeUse(tree) {
        return tree.reduce((acc, node) => {
            const filteredChildren = filterTreeUse(node._children || []);
            if (node.cmd === 'U' || filteredChildren.length > 0) {
                acc.push({ ...node, _children: filteredChildren });
            }
            return acc;
        }, []);
    }

    function filterTreeUnUse(tree) {
        return tree.reduce((acc, node) => {
            const filteredChildren = filterTreeUnUse(node._children || []);
            if (node.cmd === 'I' || filteredChildren.length > 0) {
                acc.push({ ...node, _children: filteredChildren });
            }
            return acc;
        }, []);
    }


    const updateCmd = (idList, newCmd) => {
        let isUpdated = false;

        idList.forEach(menuId => {
            if (recursiveUpdate(menuId, newCmd)) {
                isUpdated = true;
            }
        });

        if (isUpdated) {
            useMenuTree.value = filterTreeUse(menuTree.value);
            unUseMenuTree.value = filterTreeUnUse(menuTree.value);
        }
    };

    const recursiveUpdate = (menuId, newCmd) => {
        let isModified = false;

        function findAndUpdate(node, targetId) {
            if (node.menuId === targetId) {
                if (node.cmd !== newCmd) {
                    node.cmd = newCmd;
                    isModified = true;
                }
            } else if (node._children) {
                node._children.forEach(child => findAndUpdate(child, targetId));
            }
        }

        menuTree.value.forEach(root => findAndUpdate(root, menuId));

        return isModified;
    };



    return { gridGroupColumns, gridMemberColumns, gridGroupMenuColumns, menuTree, useMenuTree, unUseMenuTree, updateCmd, flattenTree, setMenuTree };
});
