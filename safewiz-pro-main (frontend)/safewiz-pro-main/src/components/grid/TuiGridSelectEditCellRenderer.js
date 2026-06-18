export default class gridSelectboxRenderer {
    constructor(props) {
        const listItems = props.columnInfo.renderer.data;
        const {codeValue, codeName, funcNm} = props.columnInfo.renderer.options;
        if (listItems != null) {
            const selectEl = document.createElement('select');
            selectEl.setAttribute('id', 'gridSelectboxRenderer_' + props.rowKey + '_' + codeValue);
            selectEl.setAttribute('class', 'gridSelect');

            if (funcNm) {
                selectEl.onchange = function () { eval(funcNm + '(' + props.rowKey + ')'); };
            }

            const optionFirstEl = document.createElement('option');
            optionFirstEl.setAttribute('value', '');
            optionFirstEl.appendChild(document.createTextNode(' '));
            // optionFirstEl.setAttribute('selected', true);
            selectEl.appendChild(optionFirstEl);

            listItems.forEach(item => {
                const optionEl = document.createElement('option');
                optionEl.value = item[codeValue];
                optionEl.textContent = item[codeName];

                if (props.value == item[codeValue]) {
                    optionEl.selected = true;
                }

                selectEl.appendChild(optionEl);
            });

            // for (let j = 0; j<listItems.length; j++) {
            //     const optionEl = document.createElement('option');
            //     if (props.value == listItems[j][codeValue]) {
            //         optionEl.setAttribute('selected', true);
            //     }
            //     optionEl.setAttribute('value', listItems[j][codeValue]);
            //     optionEl.appendChild(document.createTextNode(listItems[j][codeName]));
            //     selectEl.appendChild(optionEl);

            // }



            this.el = selectEl;
        } else {
            this.el = document.createElement('div');

        }
    }
    getElement () {
        return this.el;
    }
    getValue () {
        return this.el.selectedOptions[0].value;
    }
    mounted () {
        this.el.selectedOptions[0].label;
    }
    render (props) {
        this.el.value = String(props.value);
    }


}
