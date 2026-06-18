import { defineStore } from "pinia"
import {ref, computed} from "vue"
import _ from 'lodash'
import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
export const useMenuByUserStore = defineStore("menuByUser", () => {
    const gridUserMenuColumns = ref([
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
            column: '권한 종류',
            name: 'grpId',
            align: 'center',
            hidden: true
        },
        {
            column: '메뉴 아이디',
            name: 'menuId',
            align: 'center'
        },
        {
            column: '메뉴명',
            name: 'menuNm',
            align: 'left'
        },
        {
            column: '타입',
            name: 'cmdNm',
            align: 'center'
        },
        // {
        //     column: '조회',
        //     name: 'schAh',
        //     align: 'center',
        //     width: 40,
        //     renderer: {
        //         type: 'checkbox'
        //     }
        // },
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

    const menuTree=ref([])
    const originMenuTree = ref([]);
    const setOriginMenuTree = (list)=>{
        originMenuTree.value = _.cloneDeep(list)
    }
    const setMenuTree = (list)=>{
        menuTree.value=list
    }
    
    const useMenuTree=computed(()=>{
        return filterTreeUse(menuTree.value)
    })
    const unUseMenuTree=computed(()=>{
        return filterTreeUnUse(menuTree.value)
    })

    const flattenTree=computed(()=>{
        let result = [];
      
        function recursiveFlatten(node) {
          result.push(node);
      
          if (node._children && node._children.length > 0) {
            node._children.forEach(child => recursiveFlatten(child));
          }
        }
      
        useMenuTree.value.forEach(node => recursiveFlatten(node));
      
        return result;
    })

    function filterTreeUse(tree) {
        
        return tree.map(node => {
            const filteredChildren = filterTreeUse(node._children || []);
    
            if (node.cmd === 'U' || node.cmd ==='G') {
                node.cmdNm = node.cmd=='U'?'사용자별 권한':'그룹별 권한'
                return {
                ...node,
                _children: filteredChildren
                };
            }
    
            return null;
            }).filter(node => node !== null);
      }

    function filterTreeUnUse(tree) {
        return tree.map(node => {
        const filteredChildren = filterTreeUnUse(node._children || []);

        if (node.cmd === 'I' || filteredChildren.length > 0) {
            return {
            ...node,
            _children: filteredChildren
            };
        }

        return null;
        }).filter(node => node !== null);
    }


    const updateCmd=(idList, newCmd)=>{
        function updateParentCmd(node, parent) {
            if (newCmd === 'U' && parent) {
              parent.cmd = 'U';
              updateParentCmd(parent, parent._parent);
            }
          }
        function recursiveUpdate(node, parent=null) {
          if (idList.includes(node.menuId)) {
            node.cmd = newCmd;
            if (newCmd === 'U' && parent) {
                updateParentCmd(node, parent);
            }
          }
      
          if (node._children && node._children.length > 0) {
            node._children.forEach(child => recursiveUpdate(child,node));
          }
        }
      
        menuTree.value.forEach(node => recursiveUpdate(node));
      }
    
    return {
        gridUserMenuColumns,
        menuTree,originMenuTree, useMenuTree,unUseMenuTree,updateCmd,flattenTree,
        setMenuTree, setOriginMenuTree}
})