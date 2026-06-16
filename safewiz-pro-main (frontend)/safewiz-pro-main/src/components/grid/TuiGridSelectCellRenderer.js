export default class TuiGridFunctionButtonRenderer {
    constructor(props) {
        const  listItems  = props.columnInfo.renderer.data;

        const viewEl = document.createElement('div');
        if (listItems != null) {
            viewEl.setAttribute('class', 'tui-grid-cell-content');
            for (var j = 0; j < listItems.length; j++) {
                if (props.value == listItems[j].code) {
                    viewEl.textContent = listItems[j].codeNm;
                }
            }
        }
        this.el = viewEl;
    }
    getElement () {
        return this.el;
    }
    getValue () {
        return this.el.selectedOptions[0].value;
    }
    render (props) {
        const listItems = props.columnInfo.renderer.data;
        const codeValue = props.columnInfo.renderer.options.codeValue;
        const codeName = props.columnInfo.renderer.options.codeName;

        // props.value와 일치하는 minorCd를 찾아서 그에 해당하는 minorNm을 가져옴
        const selectedCodeNm = listItems.find(item => item[codeValue] === props.value) ?.[codeName] || '';

        this.el.value = String(props.value);
        this.el.textContent = selectedCodeNm; // minorNm으로 설정

    }
}