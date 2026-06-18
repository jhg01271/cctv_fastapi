import { initializeApp } from "firebase/app";
import { getMessaging } from "firebase/messaging";
import { getToken, onMessage } from 'firebase/messaging'

const firebaseConfig = {  
  apiKey: "AIzaSyBThVqGaxgSeUoO6xQA244sVQAjBZ6pY28",
  authDomain: "safewiz-pro.firebaseapp.com",
  projectId: "safewiz-pro",
  storageBucket: "safewiz-pro.firebasestorage.app",
  messagingSenderId: "618183838195",
  appId: "1:618183838195:web:8bc85c0f9dba04532a2a86"
};

const firebaseApp = initializeApp(firebaseConfig);
const messaging = getMessaging(firebaseApp);


export { firebaseApp, messaging, getToken, onMessage };