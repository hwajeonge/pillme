// import './assets/main.css'

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
// import "tailwindcss";
import router from './router';
// import '@/registerServiceWorker'; // ✅ PWA 서비스 워커 등록

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount('#app');

// ✅ 네트워크 상태 감지 (오프라인 감지 시 경고 표시)
window.addEventListener('online', () => console.log('[PWA] 온라인 상태입니다.'));
window.addEventListener('offline', () => console.warn('[PWA] 오프라인 상태입니다.'));

// 🚨 배포 시 주석 처리 필요: 개발 중에만 사용할 디버깅용 로그
if (import.meta.env.MODE === 'development') {
  console.log('[PWA] 개발 모드에서 실행 중...');
}

// 🚨 배포 시 주석 처리 필요: 새로고침 전에 사용자 입력 정보 저장 기능
window.addEventListener('beforeunload', () => {
  console.log('[PWA] 사용자가 페이지를 떠나기 전에 데이터를 저장해야 할 수도 있음.');
  // TODO: 사용자가 입력하던 데이터를 로컬스토리지에 저장하는 기능 추가 가능
});
