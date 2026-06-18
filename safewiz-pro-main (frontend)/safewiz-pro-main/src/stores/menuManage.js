// import { ref} from "vue";
// import { defineStore } from "pinia";
// import { menuByParam, saveMenuAll } from "@/stores/api/Menu.js";
// import _ from 'lodash';

// export const useMenuManageStore = defineStore("menuManage", () => {
// 	const menuRef = ref([]);
// 	let OriginalMaxMenuId = 0;
// 	const setMenu = () => {
// 		return new Promise((resolve, reject) => {
// 			menuByParam({type:'ALL',search:''})
// 				.then((res) => {
// 					if (res.success) {
// 						menuRef.value = [];
// 						OriginalMaxMenuId=Math.max(...res.list.map(item => parseInt(item.menuId)));
// 						handleMenu(res.list);
// 						resolve({ success: true });
// 					}
// 				})
// 				.catch((e) => {
// 					reject({ success: false, error: e });
// 				});
// 		});
// 	};
// 	const handleMenu = (list) => {
// 		const menuMap = new Map();
// 		list.forEach((item) => {
// 			item.text = item.menuNm;
// 			if (item.parentId === "0") {
// 				item.children = [];
// 				menuMap.set(item.menuId, item.children);
// 				menuRef.value.push(item);
// 			} else {
// 				const parentMenu = menuMap.get(item.parentId);
// 				if (parentMenu) {
// 					parentMenu.push(item);
// 				}
// 			}
// 		});
// 		menuRef.value.forEach((item) => {
// 			item.children.sort((a, b) => a.sort - b.sort);
// 		});
// 		menuRef.value.sort((a, b) => a.sort - b.sort);
// 	};

// 	const flattenTree = (tree)=> {
// 		let result = [];

// 		function traverse(node, parentId = "0", sortOrder = 0) {
// 			let newNode = _.cloneDeep(node);

// 			newNode.parentId = parentId;
// 			newNode.sort = sortOrder;
// 			if (newNode.text) {
// 				newNode.menuNm = newNode.text;
// 			}
// 			delete newNode.text

// 			result.push(newNode);

// 			if (newNode.children && newNode.children.length > 0) {
// 				for (let i = 0; i < newNode.children.length; i++) {
// 					traverse(newNode.children[i], newNode.menuId, i);
// 				}
// 			}

// 			delete newNode.children;
// 		}

// 		for (let i = 0; i < tree.length; i++) {
// 			traverse(tree[i], "0", i);
// 		}

// 		return result;
// 	}

//     const findMaxMenuId = ()=>{
// 		let maxMenuId = 0;
	
// 		function traverse(node) {
// 			// 현재 노드의 menuId가 가장 크면 갱신
// 			const currentMenuId = parseInt(node.menuId);
// 			if (currentMenuId > maxMenuId) {
// 				maxMenuId = currentMenuId;
// 			}
	
// 			// 자식 노드들을 탐색
// 			if (node.children && node.children.length > 0) {
// 				node.children.forEach(child => traverse(child));
// 			}
// 		}
	
// 		// 초기 트리의 각 최상위 노드에 대해 탐색 시작
// 		menuRef.value.forEach(rootNode => traverse(rootNode));
	
// 		return maxMenuId>OriginalMaxMenuId?maxMenuId:OriginalMaxMenuId;
// 	}
	
// 	const saveAll = ()=>{
// 		return new Promise((resolve, reject) => {
// 			saveMenuAll(flattenTree(menuRef.value))
// 				.then((res) => {
// 					if (res.success) {
// 						console.log(res)
// 						setMenu
// 					}
// 				})
// 				.catch((e) => {
// 					reject({ success: false, error: e });
// 				});
// 		});
// 	}


// 	return { setMenu, menuRef, findMaxMenuId, flattenTree, saveAll };
// });
