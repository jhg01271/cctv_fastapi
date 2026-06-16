import eventBus from '@/components/grid/eventBus'

export default class TuiGridFunctionButtonRenderer {
    constructor(props) {
        // const { viewDetailFunction, deleteFunction } = props.columnInfo.renderer.options
        const buttonArray = props.columnInfo.renderer.button;

        this.grid = props.grid.el.id
        this.columnInfo = props.columnInfo;
        this.rowKey = props.rowKey;

        this.elDiv = document.createElement('div');
        
        this.elDiv.classList = 'form ui';

        if (buttonArray.length > 0) {

            buttonArray.forEach(item => {

                if (item === 'view') {
                    this.createButton('보기', 'btn us radius info', this.onViewButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                } else if (item === 'delete') {
                    this.createButton('삭제', 'btn us radius warning', this.onDeleteButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                } else if (item === 'save') {
                    this.createButton('저장', 'btn us radius success', this.onSaveButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                } else if (item === 'cancel') {
                    this.createButton('취소', 'btn us radius normal', this.onCancelButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                } else if (item === 'edit') {
                    this.createButton('수정', 'btn us radius info', this.onEditButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                } else if (item === 'detail') {
                    this.createButton('상세보기', 'btn us radius info', this.onEditButtonClick.bind(this)); // 보기 버튼을 생성합니다.
                }
            });
        }




    }

    getElement () {
        return this.elDiv;
    }

    // // 보기 버튼 클릭 이벤트 핸들러
    onViewButtonClick () {
        //const equipmentStore = useEquipmentStore()
        // viewAction 이벤트를 발생시킵니다.
        eventBus.emit('viewAction', this.rowKey, this.grid);
        // this.equipmentStore.viewEquipment(this.rowKey, this.grid)
    }
    onDeleteButtonClick () {
        console.log(22)
        // deleteAction 이벤트를 발생시킵니다.
        eventBus.emit('deleteAction', this.rowKey, this.grid);
    }
    onSaveButtonClick () {
        // deleteAction 이벤트를 발생시킵니다.
        eventBus.emit('saveAction', this.rowKey, this.grid);
    }
    onCancelButtonClick () {
        // deleteAction 이벤트를 발생시킵니다.
        eventBus.emit('cancelAction', this.rowKey, this.grid);
    }
    onEditButtonClick () {
        // deleteAction 이벤트를 발생시킵니다.
        eventBus.emit('editAction', this.rowKey, this.grid);
    }
    createButton (spanText, buttonClassName, clickHandler) {
        const el = document.createElement('button');
        el.type = 'button';
        el.classList = buttonClassName;

        const elSpan = document.createElement('span');
        elSpan.textContent = spanText;

        el.addEventListener('click', clickHandler);

        el.appendChild(elSpan);
        this.elDiv.appendChild(el);
    }


    // 버튼을 생성하는 메서드
    // createButton (label, iconClassName, clickHandler) {
    //     const el = document.createElement('a');
    //     el.href = 'javascript:void(0);';
    //     el.classList = 'me-2';
    //     const elIcon = document.createElement('i');
    //     elIcon.classList = iconClassName + ' mdi v-icon';
    //     elIcon.textContent = label; // 여기서 라벨을 elIcon에 설정합니다.

    //     el.addEventListener('click', clickHandler);

    //     el.appendChild(elIcon);
    //     this.elDiv.appendChild(el);
    // }
}