
export default class CheckboxRenderer {
    constructor(props) {
        this.el = document.createElement('input');
        this.el.type = 'checkbox';

        // 초기 상태 설정
        this.render(props);
        
        // 이벤트 리스너
        this.el.addEventListener('change', () => {
            const newValue = this.el.checked ? 'Y' : 'N';
            props.grid.setValue(props.rowKey, props.columnInfo.name, newValue);
            if(newValue ==='Y'){
                props.grid.check(props.rowKey)
            }
            this.handleCheckStateRecursive(props.grid,props.rowKey, props.columnInfo.name, newValue);
            this.handleCheckStateParent(props.grid,props.rowKey, props.columnInfo.name, newValue);
        });
      
    }

    getElement() {
        return this.el;
    }

    render(props) {
        this.el.checked = props.value === 'Y';
        // 체크박스 disable 
      if (props.columnInfo.editor.options.editor){
        this.el.disabled = props.columnInfo.editor.options.editor
      }
    }

    handleCheckStateRecursive = (grid,rowKey, name, newValue) => {
        const children = grid.getChildRows(rowKey);
        // if (isChildren.value) return;
        children.forEach(child => {
            grid.setValue(child.rowKey,name,newValue);
            this.handleCheckStateRecursive(grid, child.rowKey, name,newValue); // 자식의 자식들도 재귀적으로 uncheck 처리
        });
    };

    handleCheckStateParent = (grid, key, name, newValue) => {
        // 자식 노드 선택시 부모 노드도 같이 체크 반영되는 기능 제거를 위해 주석 처리
        // const parent = grid.getParentRow(key);
        // if (parent) {
        //     if (newValue ==='Y') {
        //         grid.setValue(parent.rowKey,name,newValue);
        //     }
        //     this.handleCheckStateParent(grid,parent.rowKey, name, newValue);
        // }
    }
}