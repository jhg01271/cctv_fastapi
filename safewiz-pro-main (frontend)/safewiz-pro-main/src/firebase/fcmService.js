
import { messaging, getToken } from '@/firebase/firebaseInit'
import { defineStore } from 'pinia'
import { updateFcmToken } from '@/stores/system/user/my/MyPage.js';
import { useUserInfoStore } from '@/stores/user.js';
import BaseView from '@/components/base/BaseView';
import { ref } from 'vue'
import router from '@/router'
import { getAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
import { updateReadAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
//최초 로그인 시 권한 허용 여부 확인

export const useFcmStore = defineStore('fcm', () => {
  const alarmCount = ref(0) //알람 카운트
  
  const requestPermission = async () => {
    const { openLoading, endLoading, toastPopup } = BaseView();
    const userInfoStore = useUserInfoStore();
    try{
      const reg = await navigator.serviceWorker.ready
      const subscription = await reg.pushManager.getSubscription()  //푸시 알림 허용 여부 판별
      if(subscription == null){//허용되어 있지 않을 경우
        const permission = await Notification.requestPermission() //허용 요청 팝업창 띄움
        if (permission !== 'granted') {
          console.warn('🔒 알림 권한이 거부됨', permission)
          return permission
        }
        try {
          openLoading()
          const token = await getToken(messaging, {
            vapidKey: 'BD8bIpk4zCmD1fLQOjw9nkKUsKUCAzt9-MnWzL-AB6ymutRWUXGX-ukcuTFaF626PyONxxxRnLbgcCI3jER5Dmk'
          }) 
      
          if (token) {
            console.log('✅ FCM 토큰:', token)
            if(userInfoStore.fcmToken !== token){
              updateFcmToken({fcmToken : token}, true).then(res => {
                userInfoStore.fcmToken = res.result
              })
              endLoading() 
              return true
            }
            endLoading() 
            return permission
          }
        } catch (err) {
          console.error('❌ FCM 토큰 요청 실패:', err)
          endLoading() 
        }
      }
    }catch(error){
        console.log("에러발생 ::: ",error)
        endLoading() 
    }
  }

  navigator.serviceWorker.ready.then(() => {
    navigator.serviceWorker.addEventListener("message", (event) => {
      const toastPopupStore = useToastPopupStore();
      if (event.data?.messageType !== "push-received") { //포그라운드 메세지 형식이 아닐 경우.
        const obj = JSON.parse(event.data)
        console.log("오브젝트 타입",obj)
        if(obj.type === 'emergency'){ //비상 상황 발생
          const params = {
            alarmDt : obj.alarmDt,
            alarmSeq : obj.alarmSeq
          }
          updateReadAlarm(params, true).then(res => {
            if(res.success == true){
              getAlarm().then(res2 => {
                if(res2 && res2[0]) {
                  alarmCount.value = res2.filter(item => item.readYn === 'N').length;
                }
                else{
                  alarmCount.value = 0;
                }
              });
            }
          })
        }else if(obj.type === 'notice'){  //공지사항
          router.push({
            name: obj.routerNm,
            query: {
              fcmPayload : JSON.stringify({
                clntId : obj.clntId,
                writeDt : obj.writeDt,
                alarmDt : obj.alarmDt,
                alarmSeq : obj.alarmSeq,
                docNo: obj.docNo,
                menuId: obj.menuId
              }),
              fromPush : 'true'
            }
          });
        }else if(obj.type === 'trainingPlanImpl'){ //안전보건 교육실시 계획서
          router.push({
            name: obj.routerNm,
            query: {
              fcmPayload : JSON.stringify({
                writeYear: obj.writeYear,
                alarmDt : obj.alarmDt,
                alarmSeq : obj.alarmSeq,
                docType: obj.docType,
                docNo: obj.docNo,
                menuId: obj.menuId
              }),
              fromPush : 'true'
            }
          });
        }
      }else{
        //포그라운드일 경우, 알림뱃지 갱신 후, 메세지만 출력
        getAlarm().then(res2 => {
          if(res2 && res2[0]) {
            alarmCount.value = res2.filter(item => item.readYn === 'N').length;
          }
          else{
            alarmCount.value = 0;
          }
        });
        toastPopupStore.addToast('알림수신', '알림이 도착했습니다.', 'success')
      }
    });
  })
  return { requestPermission, alarmCount }
})
