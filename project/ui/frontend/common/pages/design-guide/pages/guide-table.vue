<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Table</h2>
    <p class="section-summary">
      테이블은 동일한 유형의 객체 집합을 표시합니다. 주된 역할은 사용자가 객체들을 한눈에 파악하여 작업을 수행하거나 전체 페이지 표현으로 이동할 수 있도록 돕는 것입니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Horizontal</h3>
      <div class="flex flex-col">
        <div class="flex flex-col gap-8 mb-14">
          <figure class="section-figure">
            <table>
              <colgroup>
                <col style="width: 15%" />
                <col style="width: 35%" />
                <col style="width: 15%" />
                <col style="width: 35%" />
              </colgroup>
              <tr>
                <th>Header Cell</th>
                <td colspan="3">Row Cell</td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>
                  There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                  alteration in some form, by injected humour
                </td>
                <th>Header Cell</th>
                <td>
                  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s is simply dummy text of the printing and typesetting
                  industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s is simply dummy
                  text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text
                  ever since thstandard dummy text ever since the 1500s
                </td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>Row Cell</td>
                <th>Header Cell</th>
                <td>Row Cell</td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>Row Cell</td>
                <th>Header Cell</th>
                <td>Row Cell</td>
              </tr>
            </table>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>

        </div>
      </div>
    </article>
    <article class="mb-10"></article>
    <h3 class="section-subtitle">Vertical</h3>
    <div class="flex flex-col">
      <div class="flex flex-col gap-8 mb-14">
        <figure class="section-figure">
          <table>
            <colgroup>
              <col style="width: 25%" />
              <col style="width: 25%" />
              <col style="width: 25%" />
              <col style="width: 25%" />
            </colgroup>
            <tr>
              <th>Header Cell</th>
              <th>Header Cell</th>
              <th>Header Cell</th>
              <th>Header Cell</th>
            </tr>
            <tr>
              <td>
                There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                alteration in some form, by injected humour
              </td>
              <td>
                is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's
                standard dummy text ever since the 1500s is simply dummy text of the printing and typesetting
                industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s is simply dummy
                text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text
                ever since thstandard dummy text ever since the 1500s
              </td>
              <td>Row Cell</td>
              <td>Row Cell</td>
            </tr>
          </table>
        </figure>
        <div class="group-description">
          <p class="group-summary">
            주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "guide-table"
};
</script>

<style scoped>

</style>