<template>
  <div class="w-full flex flex-col items-center min-h-[400px] relative">
    
    <!-- ✅ 회원 / 비회원 선택 토글 -->
    <div class="flex w-full max-w-sm relative">
      <button @click="setType('guest')"
              class="flex-1 text-center py-2 text-lg font-medium transition-all"
              :class="[type === 'guest' ? 'text-[#4E7351] border-b-2 border-[#4E7351]' : 'text-gray-400']">
        비회원
      </button>
      <button @click="setType('member')"
              class="flex-1 text-center py-2 text-lg font-medium transition-all"
              :class="[type === 'member' ? 'text-[#4E7351] border-b-2 border-[#4E7351]' : 'text-gray-400']">
        회원
      </button>
    </div>

    <!-- ✅ 로고 -->
    <img src="../assets/Logo_font.svg" alt="로고" class="w-32 mt-4 mb-6">

    <!-- ✅ 입력 필드 -->
    <div class="relative w-full max-w-sm min-h-[250px]">
      
      <!-- ✅ 비회원 입력 필드 -->
      <div v-show="type === 'guest'" class="w-full space-y-4">
        <!-- ✅ 이름 입력 -->
        <div class="flex items-center justify-between border-b border-gray-300 py-2">
          <p class="text-[#4E7351] font-medium">이름</p>
          <input v-model="guestInfo.name" 
                 type="text" 
                 placeholder="이름 입력"
                 class="w-1/2 text-right focus:outline-none"
                 :class="guestInfo.name ? 'text-black' : 'text-gray-400'" />
        </div>

        <!-- ✅ 성별 선택 -->
        <div class="flex items-center justify-between border-b border-gray-300 py-2">
          <p class="text-[#4E7351] font-medium">성별</p>
          <div class="flex space-x-2">
            <button @click="setGender('M')" 
                    :class="['px-4 py-1 rounded-lg transition-all', 
                            guestInfo.gender === 'M' ? 'bg-[#4E7351] text-white' : 'bg-gray-200 text-gray-600']">
              남
            </button>
            <button @click="setGender('F')" 
                    :class="['px-4 py-1 rounded-lg transition-all', 
                            guestInfo.gender === 'F' ? 'bg-[#4E7351] text-white' : 'bg-gray-200 text-gray-600']">
              여
            </button>
          </div>
        </div>

        <!-- ✅ 생년월일 입력 -->
        <div class="flex items-center justify-between border-b border-gray-300 py-2">
          <p class="text-[#4E7351] font-medium">생년월일</p>
          <input v-model="guestInfo.birthdate"
                 type="text"
                 placeholder="ex)1997-01-01"
                 class="w-1/2 text-right focus:outline-none"
                 :class="guestInfo.birthdate ? 'text-black' : 'text-gray-400'"
                 maxlength="10"
                 @input="formatBirthdate" />
        </div>

        <!-- ✅ 안내 문구 -->
        <p class="text-center text-sm text-[#4E7351] mt-4">
          비회원은 푸시 알림을 받을 수 없습니다.<br>
          푸시 알림을 받으시려면 회원으로 등록해주세요.
        </p>
      </div>

      <!-- ✅ 회원 입력 필드 -->
      <div v-show="type === 'member'" class="w-full space-y-4">
        <p class="text-sm text-gray-600 text-center">
          동의를 얻을 가족의 핸드폰 번호를 입력해주세요.
        </p>
        <p class="text-xs text-gray-500 text-center">
          14세 이상 회원 추가는 동의 요청 및 승인 과정을 거쳐야 합니다.
        </p>

        <!-- ✅ 전화번호 입력 & 회원등록 요청 (세로 배치) -->
        <div class="flex flex-col space-y-2 border-b border-gray-300 py-2">
          <input v-model="memberPhone" 
                 type="tel" 
                 placeholder="상대방 전화번호 인증"
                 class="w-full text-gray-500 focus:outline-none focus:border-[#3D5A3F]" />
          <button @click="verifyPhone" 
                  class="w-full px-4 py-2 bg-[#4E7351] text-white rounded-lg hover:bg-[#3D5A3F]">
            회원등록 요청
          </button>
        </div>

        <!-- ✅ 오류 메시지 -->
        <p v-if="verificationError" class="text-sm text-red-500 text-center">{{ verificationError }}</p>
      </div>
    </div>

    <!-- ✅ 하단 버튼: 비회원일 때만 표시 -->
    <div v-if="type === 'guest'" class="p-4 w-full max-w-sm">
      <button @click="submitForm" 
              class="w-full py-3 bg-gray-100 text-[#4E7351] rounded-full text-lg font-semibold hover:bg-gray-200">
        완료
      </button>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { addLocalMember } from '../api/addlocalmember';
import { addMemberAlarm } from '../api/addmember';

const emit = defineEmits(["addmember"]);

// 회원/비회원 선택
const type = ref("guest");
const guestInfo = ref({ name: "", birthdate: "", gender: "" });
const memberPhone = ref("");
const verificationError = ref("");

// 비동기 요청 상태 관리
const isLoading = ref(false);

// 회원/비회원 토글
const setType = (selectedType) => {
  type.value = selectedType;
};

// 성별 선택 (M, F)
const setGender = (gender) => {
  guestInfo.value.gender = gender;
};

// 생년월일 입력 포맷팅 (YYYY-MM-DD)
const formatBirthdate = () => {
  let value = guestInfo.value.birthdate.replace(/\D/g, '');
  if (value.length > 4) value = `${value.slice(0, 4)}-${value.slice(4)}`;
  if (value.length > 7) value = `${value.slice(0, 7)}-${value.slice(7)}`;
  guestInfo.value.birthdate = value.slice(0, 10);
};

// 비회원/회원 가입 요청 (비동기 처리)
const submitForm = async () => {
  if (isLoading.value) return;
  isLoading.value = true;

  if (type.value === "guest") {
    if (!guestInfo.value.name || !guestInfo.value.birthdate || !guestInfo.value.gender) {
      alert("모든 정보를 입력해주세요.");
      isLoading.value = false;
      return;
    }
    const cleanBirthday = guestInfo.value.birthdate.replace(/-/g, '');

    try {
      await addLocalMember({
        name: guestInfo.value.name,
        gender: guestInfo.value.gender,
        birthday: cleanBirthday,
      });

      alert("비회원 추가가 완료되었습니다.");
      emit("close");
    } catch (error) {
      console.error("❌ 비회원 추가 실패:", error);
      alert("비회원 추가에 실패했습니다. 다시 시도해주세요.");
    }}};

const verifyPhone = async () => {
  if (isLoading.value) return;
  isLoading.value = true;
  
  if (!memberPhone.value) {
    alert("전화번호를 입력해주세요.");
    isLoading.value = false;
    return;
  }
  
  // 보내는 데이터 객체 생성
  const dataToSend = { phone: memberPhone.value };
  
  // 디버깅 로그: 전송할 데이터 출력
  // console.log("디버깅 로그 - addMemberAlarm에 보내는 데이터:", dataToSend);

  try {
    console.log("📨 회원 추가 알림 요청:", memberPhone.value);
    await addMemberAlarm(dataToSend);
    alert("상대방이 요청을 수락하면 나의 가족 목록에서 확인하실 수 있습니다.");
    emit("close");
  } catch (error) {
    console.error("❌ 회원 추가 알림 실패:", error);
    alert("회원 추가 알림 요청에 실패했습니다. 다시 시도해주세요.");
  }
  
  isLoading.value = false;
};


</script>
