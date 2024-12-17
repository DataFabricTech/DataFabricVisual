<template>
  <section class="guide-section">
    <p class="section-type">Foundation</p>
    <h2 class="section-title">Color</h2>
    <p class="section-summary">
      컬러는 브랜드의 아이덴티티를 표현하는 중요한 요소입니다. 지정된 색상과 사용 규정을 숙지하여 브랜드 아이덴티티의 일관성을 유지할 수 있도록 합니다.
    </p>
    <article class="mb-10">
      <div v-for="(colors, category) in colorCategories" :key="category">
        <h3 class="section-subtitle">{{ category }}</h3>
        <ol class="color-list" :class="[category.toLowerCase()]">
          <li class="color-item" v-for="color in colors" :key="color.name">
            <p>{{ color.name }}</p>
            <p>{{ color.hex }}</p>
          </li>
        </ol>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-color",
  data() {
    return {
      colorCategories: {
        Primary: [
          { name: 'Primary-900', hex: '#01353C' },
          { name: 'Primary-700', hex: '#004D57' },
          { name: 'Primary-500', hex: '#09838F' },
          { name: 'Primary-300', hex: '#6EBBC4' },
          { name: 'Primary-100', hex: '#E9F5F7' }
        ],
        Secondary: [
          { name: 'Secondary-900', hex: '#0A3C4C' },
          { name: 'Secondary-700', hex: '#115F79' },
          { name: 'Secondary-500', hex: '#188AB0' },
          { name: 'Secondary-300', hex: '#83C8DF' },
          { name: 'Secondary-100', hex: '#E4F6FB' }
        ],
        Neutral: [
          { name: 'Neutral-900', hex: '#000000' },
          { name: 'Neutral-800', hex: '#0E1422' },
          { name: 'Neutral-700', hex: '#2B3440' },
          { name: 'Neutral-600', hex: '#31353F' },
          { name: 'Neutral-500', hex: '#525866' },
          { name: 'Neutral-400', hex: '#868C98' },
          { name: 'Neutral-300', hex: '#BABFC8' },
          { name: 'Neutral-200', hex: '#DBE0E8' },
          { name: 'Neutral-100', hex: '#F4F6F9' },
          { name: 'Neutral-0', hex: '#FFFFFF' },
        ],
        Info: [
          { name: 'Info-900', hex: '#001962' },
          { name: 'Info-700', hex: '#003393' },
          { name: 'Info-500', hex: '#005ACD' },
          { name: 'Info-300', hex: '#60ABF0' },
          { name: 'Info-100', hex: '#E3F3FD' },
        ],
        Success: [
          { name: 'Success-900', hex: '#002D30' },
          { name: 'Success-700', hex: '#00493F' },
          { name: 'Success-500', hex: '#006644' },
          { name: 'Success-300', hex: '#349978' },
          { name: 'Success-100', hex: '#E8FCF6' },
        ],
        Warning: [
          { name: 'Warning-900', hex: '#7A4505' },
          { name: 'Warning-700', hex: '#CC7309' },
          { name: 'Warning-500', hex: '#FF8B00' },
          { name: 'Warning-300', hex: '#FFC766' },
          { name: 'Warning-100', hex: '#FDF6E3' },
        ],
        Error: [
          { name: 'Error-900', hex: '#661400' },
          { name: 'Error-700', hex: '#991E00' },
          { name: 'Error-500', hex: '#BF2600' },
          { name: 'Error-300', hex: '#F57251' },
          { name: 'Error-100', hex: '#FFEBE6' },
        ]
      }
    };
  }
};
</script>

<style scoped>

</style>