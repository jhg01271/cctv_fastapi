import { defineStore } from "pinia"
import {ref, computed} from "vue"
export const useMenuByAuthStore = defineStore("menuByAuth", () => {

    const menuTree=ref([])
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
    
            if (node.cmd === 'U') {
                return {
                ...node,
                _children: filteredChildren
                };
            }
    
            return null;
            }).filter(node => node !== null);
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
        menuTree,useMenuTree,unUseMenuTree,updateCmd,flattenTree,
    setMenuTree}
})