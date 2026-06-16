<template>
    <div class="contents">
        <div id="form" class="box form ui pr df fdc">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh">
                    <div class="pa2-2rem">
                        <div class="row flex gutters1rem h100p el-ha">
                            <!-- CCTV 리스트 영역 -->
                            <div class="grid12-2 h100p df fdc lg-grid12-12 lg-mb4rem">
                                <div class="segment oh wbka lh1-5">
                                    <ul class="df fdc gap4px lg-dg lg-gtc1fr-1fr-1fr-1fr-1fr sm-gtc1fr-1fr-1fr us-gtc1fr-1fr">
                                        <li v-for="(item, index) in safetyMonitoringZoneManageStore.cctvList" :key="item.cctv_id">
                                            <button type="button" class="db w100p p12px br4px b1pxsolidEEEEEE fs13px" :class="{ active: selCCTVListIndex == index }" :key="index" @click="onClickRow(item, index)">
                                              <div>
                                                <div>
                                                  <span>{{ item.camera_nm }}</span>
                                                </div>
                                                <div>
                                                  <span>{{ '(' + item.cctv_id + ')' }}</span>
                                                </div>
                                              </div>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <!-- 안전 감시 구역 설정 영역 -->
                            <div class="grid12-10 h100p df fdc lg-grid12-12">
                                <div class="df aic jcsb mb2rem us-fww">
                                    <h3 class="mb0i">{{safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex]?.camera_nm + ' (' + selCCTVID + ')'}}</h3>
                                    <div class="df aic gap4px">
                                        <button type="button" class="btn es line br4px" @click="resetDraws(true)">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-rotate-ccw-icon lucide-rotate-ccw">
                                                <path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8" />
                                                <path d="M3 3v5h5" />
                                            </svg>
                                            <span class="ml4px">초기화</span>
                                        </button>
                                        <button type="button" class="btn es line br4px" @click="handleHistory('undo')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-undo-icon lucide-undo">
                                                <path d="M3 7v6h6" />
                                                <path d="M21 17a9 9 0 0 0-9-9 9 9 0 0 0-6 2.3L3 13" />
                                            </svg>
                                            <span class="ml4px">되돌리기</span>
                                        </button>
                                        <button type="button" class="btn es line br4px" @click="handleHistory('redo')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-redo-icon lucide-redo">
                                                <path d="M21 7v6h-6" />
                                                <path d="M3 17a9 9 0 0 1 9-9 9 9 0 0 1 6 2.3l3 2.7" />
                                            </svg>
                                            <span class="ml4px">살리기</span>
                                        </button>
                                    </div>
                                </div>
                                <div class="grid-12-12 mt0 df jcsb aic">
                                    <p class="fs13px mb8px fw300 c666666 lh1-3">* 두 개 이상의 좌표가 있을 때, 우클릭한 지점을 기준으로 영역이 설정됩니다.</p>
                                    <!-- 도움말 툴팁 -->
                                    <div class="help-tooltip pr" >
                                      <div class="help-icon" @mouseenter="showTooltip = true" @mouseleave="showTooltip = false">?</div>
                                      <Transition name="tooltip-fade">
                                          <div class="tooltip-box" v-show="showTooltip">Ctrl + 휠</div>
                                      </Transition>
                                      <div class="zoom-control">
                                          <button type="button" @click="zoomOut">−</button>
                                          <span>{{ zoomLevel }}%</span>
                                          <button type="button" @click="zoomIn">+</button>
                                      </div>
                                    </div>

                                </div>
                                <div class="row flex gutters1rem">
                                    <div class="grid12-12">
                                        <div class="pr df aic jcc h70rem bgEEEEEE custom-scroll oa db pr" @wheel.ctrl.prevent="handleWheel">

                                            <v-sheet :width="scaleWidth * (scaleFactor * 0.1)" :height="scaleHeight * (scaleFactor * 0.1)" :style="`aspect-ratio: ${imageRatio}; flex-shrink: 0;`">
                                                <v-stage 
                                                  :config="{
                                                    width : stageConfig.width * (scaleFactor * 0.1),
                                                    height : stageConfig.height * (scaleFactor * 0.1),
                                                    scaleX: scaleFactor * 0.1,
                                                    scaleY: scaleFactor * 0.1
                                                  }"
                                                  @contextmenu="onHandleRightClick"
                                                  @mouseenter="handleMouseEnter"
                                                  @mouseleave="handleMouseLeave"
                                                  @click="onHandleLeftClick"
                                                >
                                                    <v-layer>
                                                        <v-image 
                                                            :config="{
                                                              image : cctvImg, 
                                                              width: stageConfig.width,
                                                              height: stageConfig.height, 
                                                              x: scaleFactor * 0.1, 
                                                              y: scaleFactor * 0.1
                                                            }" 
                                                            :style="`aspect-ratio: ${imageRatio};`"
                                                        />
                                                    </v-layer>
                                                    <v-layer>
                                                        <v-group 
                                                            :config="{draggable: true}"
                                                            @dragend="dragGroup"
                                                        >
                                                            <v-line :config="triangleConfig" />
                                                            <v-circle v-for="(p, i) in points" :key="i"
                                                                :config="{x: p.x, y: p.y, radius: 5, fill: 'red', draggable: true}"
                                                                @dragmove="dragPoint($event, i)"
                                                                @dragend=" handleDragEnd($event)"
                                                            />
                                                        </v-group>
                                                    </v-layer>
                                                </v-stage>
                                            </v-sheet>
                                        </div>
                                    </div>
                                    <div class="grid12-6 lg-grid12-12">
                                        <h4 class="fc3248F6 mb1rem">위험구역 좌표</h4>
                                        <div class="coord-list">
                                            <span v-for="(v, i) in points" :key="i">
                                                {{ v.x +',' + v.y }}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="grid12-6 lg-grid12-12">
                                        <h4>모델 선택</h4>
                                        <div class="model-list">
                                            <ul class="df fdc gap8px">
                                                <li>
                                                    <input v-model="checkedDetection" type="checkbox" v-input="'안전모 미착용, 위험구역 출입'" @click="selectedModel('Detection')"/>
                                                </li>
                                                <li>
                                                    <input v-model="checkedPose" type="checkbox" v-input="'작업자 쓰러짐'" @click="selectedModel('Pose')"/>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, computed, onMounted, t, btnBack, btnSearch, btnSave, openLoading, endLoading } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useSafetyMonitoringZoneManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyMonitoringZoneManage.js';
const safetyMonitoringZoneManageStore = useSafetyMonitoringZoneManageStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();

const originalWidth = ref(0) // 이미지 원본 너비
const originalHeight = ref(0) // 이미지 원본 높이
const inputWidth = ref(900)
const inputHeight = ref(600)
const imageRatio = ref(16/9)
const cctvImg = ref(null)
const scaleWidth = ref(0)
const scaleHeight = ref(0)
const isScaleFactor = ref(false)
const scaleFactor = ref(10)
const isPolygon = ref(false)
const legacyList = ref([])

const checkedDetection = ref(false)
const checkedPose = ref(false)

const stageConfig = ref({
  width: 1920,
  height: 1080
})


const points = ref([]);

const triangleConfig = computed(() => ({
  points: points.value.flatMap(p => [p.x, p.y]),
  fill: 'rgba(50, 72, 246, 0.3)',
  stroke: '#3248F6',
  strokeWidth: 2,
  lineJoin : 'round',
  closed: isPolygon.value,
  fill: isPolygon.value ? 'rgba(0, 255, 0, 0.4)' : '',
}));


const modifyPoints = ref([])

onMounted(async () => {
  await userInfoStore.getUserInfo();
  //safetyMonitoringZoneManageStore.userCd = userInfoStore.loginId;
  safetyMonitoringZoneManageStore.userCd = 'IGNS_esg_team';

  console.log('# 유저 정보', safetyMonitoringZoneManageStore.userCd);
  //const param = setRouterParam();

  layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave'];

  // CCTV 리스트 조회
  await safetyMonitoringZoneManageStore.searchListData();
  const cctvListData = safetyMonitoringZoneManageStore.cctvList
  cctvListData.map(el => {
    const { model_list, ...rest } = el
    const detection = model_list.find(el => el.model_nm == 'Detection')
    const pose = model_list.find(el => el.model_nm == 'Pose')?.is_run
    const coord = JSON.parse(detection['point_arr'] || "[]")
    return {
      ...rest,
      point_arr: coord,
      detection: detection['is_run'] || false,
      isPolygon: coord.length >= 3 ? true : false,
      pose: pose || false
    }
  })
  legacyList.value = JSON.parse(JSON.stringify(cctvListData))
  if(cctvListData.length !== 0){
    if(selCCTVID.value == null){
      selCCTVID.value = cctvListData[0].cctv_id
      isPolygon.value = cctvListData[0].isPolygon
    }
  }

  if (cctvListData.length > 0) {
    onClickRow(cctvListData[0], selCCTVListIndex.value);
  }
  console.log('# CCTV 리스트 정보#1', cctvListData);
  console.log('# CCTV 리스트 정보#2', cctvListData[0].cctv_id);
});

// 목록 버튼 이벤트 함수
btnBack(() => {
  safetyMonitoringZoneManageStore.btnBack();
});

// 조회 버튼 이벤트 함수
btnSearch(async () => {
  await safetyMonitoringZoneManageStore.btnSearch();
  const foundData = safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].model_list.find(val => val.point_arr !== '[]');
  modifyPoints.value[selCCTVListIndex.value] = undefined;
  resetDraws(true, true)
  points.value = JSON.parse(foundData.point_arr).map(([x, y]) => ({ x, y }))
  history.value.push([...points.value])
  foundData.cctv_id = safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].cctv_id
  onClickRow(foundData, selCCTVListIndex.value)
});

// 저장 버튼 이벤트 함수
btnSave(async () => {
  await safetyMonitoringZoneManageStore.btnSave(selCCTVID.value, points.value);
});

//#region 배율 조정
const showTooltip = ref(false);
const zoomLevel = ref(100);

//이미지 확대
const zoomIn = () => {
  if (zoomLevel.value < 200) {
      zoomLevel.value += 10;
      scaleFactor.value += 1
  }
};

//이미지 축소
const zoomOut = () => {
  if (zoomLevel.value > 10){
      zoomLevel.value -= 10
      scaleFactor.value -= 1
  }
};

//ctrl + 마우스 휠 이벤트
const handleWheel = (e) => {
  if (e.ctrlKey) {
    if(e.deltaY < 0){
        zoomIn()
    }else{
        zoomOut()
    }
  }
}
//#endregion

//#region CCTV 리스트 영역 관련
const selCCTVListIndex = ref(0); // 선택된 CCTV 리스트의 index 값 (최초 진입 시 0)
const selCCTVID = ref(''); // 선택된 CCTV 리스트의 ID 값
const selCCTVList = ref(null) //선택된 CCTV 리스트 데이터

const onClickRow = async (item, index) => {
  selCCTVListIndex.value = index;
  selCCTVID.value = item.cctv_id;
  points.value = []
  history.value = []
  historyIndex.value = -1
  handleImage(selCCTVID.value)
};
//#endregion

//#region CCTV 이미지 영역 관련
const handleImage = async(param) => {
  openLoading()
  await safetyMonitoringZoneManageStore.getCCTVImage(param)
  console.log("safetyMonitoringZoneManageStore.imageData",safetyMonitoringZoneManageStore.imageData)
  if(safetyMonitoringZoneManageStore.imageData === null){
    checkedDetection.value = false
    checkedPose.value = false
    cctvImg.value = null
    return
  }
  const image = safetyMonitoringZoneManageStore.imageData
  if(image){
    originalWidth.value = image.img_size.width
    originalHeight.value = image.img_size.height
    inputWidth.value = image.img_size.width
    inputHeight.value = image.img_size.height
    let [x, y] = image.ratio ? image.ratio.split(':').map(Number) : [4, 3]
    imageRatio.value = x/y
    const byteCharacters = atob(image.img_decode_data)
    const byteNumbers = new Array(byteCharacters.length).fill().map((_, i) => byteCharacters.charCodeAt(i))
    const byteArray = new Uint8Array(byteNumbers)
    const blob = new Blob([byteArray], { type: 'image/jpeg' })
    createImg(URL.createObjectURL(blob))
    endLoading()
  }else{
    endLoading()
  }
}

const createImg = (imgParam) => {
  openLoading()
  const img = new Image()
  img.src = imgParam
  const image = safetyMonitoringZoneManageStore.imageData
  img.onload = () => {
      cctvImg.value = img
      originalWidth.value = image.img_size.width
      originalHeight.value = image.img_size.height
      if(isScaleFactor) {
          scaleWidth.value = image.img_size.width * scaleFactor.value
          scaleHeight.value = image.img_size.height * scaleFactor.value
      }else{
          scaleWidth.value = inputWidth.value
          scaleHeight.value = inputHeight.value
      }
      //stageConfig.value.width = scaleWidth.value / 20
      //stageConfig.value.height = scaleHeight.value / 20 //***************************************************수정해야함****************************************************************
      stageConfig.value.width = 951
      stageConfig.value.height = 700
  }
  const cctvData = safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].model_list.find(val => val.point_arr !== '[]')
  let pointArr = []
  if(cctvData !== undefined){
    pointArr = JSON.parse(cctvData.point_arr).map(([x, y]) => ({ x, y }))
  }
   
  if(modifyPoints.value[selCCTVListIndex.value] !== undefined && points.value !== undefined){
    points.value = modifyPoints.value[selCCTVListIndex.value]
  }else{
    points.value = [...pointArr];
  }
  checkedIsPolygon()
  
    safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].model_list.forEach(val => {
    if(val.model_nm === 'Detection'){
      if(val.is_run === true){
        checkedDetection.value = true
      }else{
        checkedDetection.value = false
      }
    }else if(val.model_nm === 'Pose'){
      if(val.is_run === true){
        checkedPose.value = true
      }else{
        checkedPose.value = false
      }
    }
  })

  history.value.push([...points.value])
  endLoading()
}
//#endregion

//#region 드래그 이벤트 관련

// 점 드래그 이벤트
const dragPoint = (e, index) => {
  const { x, y } = e.target.attrs;
  points.value[index] = { x, y };
  modifyPoints.value[selCCTVListIndex.value] = points.value

};

// 면 드래그 이벤트
const dragGroup = (e) => {
  const group = e.target
  const groupX = group.x()
  const groupY = group.y()

  points.value = points.value.map(val => ({
      x : val.x + groupX,
      y : val.y + groupY
  }))

  modifyPoints.value[selCCTVListIndex.value] = points.value
  history.value.push([...points.value]);
  group.x(0);
  group.y(0);

};

const onHandleLeftClick = (e) => {
  if(safetyMonitoringZoneManageStore.imageData == null){
    return
  }
  if(!isPolygon.value){
    isPolygon.value = false
    const stage = e.target.getStage();
    const pointerPosition = stage.getPointerPosition();

    // ★ 중요: 줌(scale)이나 드래그 상태를 역산해서 실제 이미지 좌표를 구함
    const transform = stage.getAbsoluteTransform().copy().invert();
    const relativePos = transform.point(pointerPosition);
    points.value.push({ x: relativePos.x, y: relativePos.y });
    history.value.push([...points.value]);
  }

}
  const onHandleRightClick = (e) => {
  if(safetyMonitoringZoneManageStore.imageData == null){
    return
  }
    e.evt.preventDefault();
    if(isPolygon.value === false){
    const stage = e.target.getStage();
    const pointerPosition = stage.getPointerPosition();
    const transform = stage.getAbsoluteTransform().copy().invert();
    const relativePos = transform.point(pointerPosition);

    const isDuplicate = points.value.some(
      (p) => p.x === relativePos.x && p.y === relativePos.y
    );
    if(!isDuplicate){
      points.value.push({ x: relativePos.x, y: relativePos.y });
      history.value.push([...points.value]);
    }
    if(points.value.length >= 3){
      endDraws()
    }
    }
  }

  const endDraws = () => {
    isPolygon.value = true
  }

    const handleMouseEnter = (e) => {
      const stage = e.target.getStage()
      stage.container().style.cursor = isPolygon.value ? 'pointer' : 'default'
    }
    const handleMouseLeave = (e) => {
      const stage = e.target.getStage()
      stage.container().style.cursor = isPolygon.value ? 'pointer' : 'default'
    }

//#endregion

//#region 초기화, 되돌리기, 살리기 버튼 관련
const history = ref([]);
const historyIndex = ref(-1)

const handleDragEnd = (e) => {
  e.cancelBubble = true
  history.value.push([...points.value]);
}

//초기화 버튼 이벤트
const resetDraws = (isForce, isPolygonData) => {
  if(isForce){
    points.value = []
    history.value = []
    historyIndex.value = -1
  }
  if(isPolygonData){
    isPolygon.value = isPolygonData
  }else{
    isPolygon.value = false
  }

}
  const checkedIsPolygon = () => {
    if(points.value.length >= 3){
      isPolygon.value = true
    }else{
      isPolygon.value = false
    }
  }

//되돌리기 및 살리기 버튼 이벤트
const handleHistory = async(type) => {
  if (history.value.length === 0) {
    return;
  }
  if (history.value.length > 0) {
    if(type === 'undo'){ // 되돌리기
      if(Math.abs(historyIndex.value) != history.value.length){
        historyIndex.value--
      }
      points.value = JSON.parse(JSON.stringify(history.value.at(historyIndex.value)));
      checkedIsPolygon()

    }else if(type === 'redo'){ //살리기
      if(historyIndex.value < -1){
        historyIndex.value++
      }
      points.value = JSON.parse(JSON.stringify(history.value.at(historyIndex.value)));
      checkedIsPolygon()
    }
  }
};
//#endregion

//#region 모델 선택 이벤트
const selectedModel = (type) => {
  const detection = safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].model_list[0]
  const pose = safetyMonitoringZoneManageStore.cctvList[selCCTVListIndex.value].model_list[1]
  if(type === 'Detection'){
    detection.is_run = !detection.is_run
  }else{
    pose.is_run = !pose.is_run
  }
}
//#endregion
</script>


<style lang="scss" scoped>
/* 위험구역 좌표 칩 */
.coord-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    span {
        display: inline-block;
        padding: 6px 14px;
        border: 1px solid #d0d0d0;
        border-radius: 4px;
        font-size: 13px;
        color: #333;
        background: #fff;
    }
}

/* 도움말 툴팁 */
.help-tooltip {
    display: flex;
    align-items: center;
    gap: 8px;

    .help-icon {
        width: 33px;
        height: 33px;
        border: 1px solid #d0d0d0;
        border-radius: 50%;
        background: #fff;
        font-size: 15px;
        color: #999;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .tooltip-box {
        position: absolute;
        bottom: 120%;
        left: 10%;
        transform: translateX(-50%);
        padding: 6px 12px;
        background: #333;
        color: #fff;
        border-radius: 4px;
        font-size: 12px;
        white-space: nowrap;
        z-index: 10;
    }
}

/* 툴팁 애니메이션 */
.tooltip-fade-enter-active,
.tooltip-fade-leave-active {
    transition:
        opacity 0.2s ease,
        transform 0.2s ease;
}
.tooltip-fade-enter-from,
.tooltip-fade-leave-to {
    opacity: 0;
    transform: translateY(4px);
}

/* 배율 조정 */
.zoom-control {
    background: #fff;
    display: flex;
    align-items: center;
    margin-left: 8px;

    border: 1px solid #d0d0d0;
    border-radius: 4px;
    overflow: auto;

    button {
        width: 32px;
        height: 32px;

        background: #fff;
        font-size: 16px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
            background: #f5f5f5;
        }

        &:first-child {
            border-right: 1px solid #d0d0d0;
        }

        &:last-child {
            border-left: 1px solid #d0d0d0;
        }
    }

    span {
        min-width: 50px;
        height: 33px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 13px;
        color: #333;
    }
}

/* CCTV 리스트 버튼 active 스타일 */
button.active {
    border-color: #3248f6 !important;
    background: rgba(50, 72, 246, 0.08) !important;
    color: #3248f6;
    font-weight: 600;
}

.custom-scroll::-webkit-scrollbar {
    width: 12px;
    height: 12px;
}
.custom-scroll::-webkit-scrollbar-thumb {
    background-color: #3248F6; /* 파란색 막대 */
    border-radius: 10px;
    border: 3px solid #EEEEEE; /* 배경과 같은 색으로 테두리 주면 훨씬 잘 보임 */
}
.custom-scroll::-webkit-scrollbar-track {
    background-color: #f1f1f1;
    border-radius: 10px;
}
</style>
