/* ---------------------------------------
	Filename: common.js
	Description : 공통 스타일
	Author	: mobigen
	date	: 2022-07
	비고 : 동작용 샘플
   --------------------------------------- */

//임시
$(document).ready(function () {
  gnbUI();
  categoryMenu();
});

/* GNB */
function gnbUI() {
	// GNB - depth1
	$(".nav > ul > li > a, .nav > ul > li > ul").on('mouseenter focusin', function(){		
		$(this).parent().addClass("active");
	}).on('mouseleave', function() {
		$(this).parent().removeClass("active");
	});
  
  // $(".nav > ul > li").on("mouseenter touchend", function (e) {
  //   e.stopPropagation();
  //   $(this).addClass("active").siblings().removeClass("active");
  //   $("header").addClass("open");
  // });

  // $(".nav > ul > li > a").on("click touchend", function (e) {
  //   e.preventDefault();
  // });

  // 서브 메뉴 current 추가시 부모 메뉴 current 자동 추가
  if ($(".nav > ul ul li").hasClass("current")) {
    $(".nav > ul ul li.current").parent().parent().addClass("current");
  }

  // 서브 메뉴 클릭시 current 삭제 및 추가
  $(".nav > ul ul li").on("click touchend", function () {
    $(".nav > ul ul li").removeClass("current");
    $(this).addClass("current").parent().parent().addClass("current").siblings().removeClass("current");

    $("header").addClass("header-sub");
    $("main").addClass("main-sub");
    $(".nav").removeClass("open");
    $("body").removeClass("no-scroll");
  });
}

/* sub-GNB */
function subGnbUI() {
  $(".nav > ul > li").on("mouseenter touchend", function (e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).addClass("active").siblings().removeClass("active");
    $(".sub-header").addClass("open");
  });

  $("body").on("focusin", function () {
    if ($(".nav li a").is(":focus")) {
      $(".sub-header").addClass("open");
    } else {
      $(".sub-header").removeClass("open");
    }
  });

  $(".nav").on("mouseleave", function () {
    $(".sub-header").removeClass("open");
  });

  // 서브 메뉴 current 추가시 부모 메뉴 current 자동 추가
  if ($(".nav > ul ul li").hasClass("current")) {
    $(".nav > ul ul li.current").parent().parent().addClass("current");
  }

  // 서브 메뉴 클릭시 current 삭제 및 추가
  $(".nav > ul ul li").on("click touchend", function () {
    $(".nav > ul ul li").removeClass("current");
    $(this).addClass("current").parent().parent().addClass("current").siblings().removeClass("current");
  });
}

// 필터 토글기능버튼 클릭시 filter-toggle
var $toggleFilterButton = $(".filter-box .filter-head .filter-toggle");
$toggleFilterButton.on("click", function () {
  if ($(this).hasClass("on")) {
    $(this).removeClass("on").children().text("펼치기");
    $(this).parent().next(".filter-body").removeClass("open");
  } else {
    $(this).addClass("on").children().text("접기");
    $(this).parent().next(".filter-body").addClass("open");
  }
});

//검색목록 검색필터 Fold
var $detailSearch = $(".data-filter-wrap");
var $filterButton = $(".content-search-wrap .inner-filter .btn-filter-toggle");

//검색목록 검색필터 Fold > 조회테이블 상세조회조건 샘플코드
var $addButton = $(".data-filter-wrap .btn-attribute-filter-toggle");
var $attributeFilterItem = $(".attribute-filter-item");

$filterButton.on("click", function () {
  $detailSearch.slideToggle();
  if ($(this).hasClass("on") == true) {
    $(this).attr("title", "검색필터펼치기");
    $(this).removeClass("on");
    $attributeFilterItem.slideUp();
    $addButton.attr("title", "추가속성필터 열기");
    $addButton.removeClass("on");
    $(this).children().text("Fliter");
  } else {
    $(this).attr("title", "검색필터접기");
    $(this).addClass("on");
    $(this).children().text("Close");
  }
});

$addButton.on("click", function () {
  $attributeFilterItem.slideToggle();
  if ($(this).hasClass("on") == true) {
    $(this).attr("title", "추가속성필터 열기");
    $(this).removeClass("on");
  } else {
    $(this).attr("title", "추가속성필터 닫기");
    $(this).addClass("on");
  }
});

var $viewButton = $(".category-wrap .category-head .btn-category-toggle");
var $categoryWrap = $(".category-wrap");
$viewButton.on("click", function () {
  if ($(this).hasClass("on") == true) {
    $(this).attr("title", "카테고리 펼쳐보기");
    $(this).removeClass("on");
    $categoryWrap.removeClass("folding");
  } else {
    $(this).attr("title", "카테고리 접어보기");
    $(this).addClass("on");
    $categoryWrap.addClass("folding");
  }
});

// 카테고리 트리형 메뉴
function categoryMenu() {
  var $categoryMenu = $(".category-menu"); // 전체메뉴
  var $treeItem = $categoryMenu.find("li");
  var $toggleItemButton = $categoryMenu.find(".item-toggle"); // 토글기능버튼 클릭시 .on추가되는 button
  var $activeItemLabel = $categoryMenu.find(".item-label"); // 선택되는 카테고리메뉴명 a tag

  $treeItem.each(function () {
    if ($(this).children().is(".sub-category-menu")) {
      // 자식 카테고리 ul
      $(this).addClass("has-sub"); // 자식 카테고리 있을 경우 .has-sub
    }
  });

  // 토글기능버튼 클릭시 li.open
  $toggleItemButton.on("click", function () {
    var $toggle = $(this);

    if ($toggle.hasClass("on")) {
      $toggle.removeClass("on").children().text("펼치기");
      $toggle.parent("li").removeClass("open");
    } else {
      $toggle.addClass("on").children().text("접기");
      $toggle.parent("li").addClass("open");
    }
  });

  // a tag 클릭시 active(선택) / current(현재 선택된active의 li)
  $activeItemLabel.on("click", function () {
    var $active = $(this);
    var $currentItem = $active.parent();

    if (!$active.hasClass("active")) {
      $treeItem.removeClass("current");
      $activeItemLabel.removeClass("active");
      $active.addClass("active");
      $currentItem.addClass("current");
    } else {
      $currentItem.removeClass("current");
      $active.removeClass("active");
    }
  });
}

$(function () {
  /* selectbox */
  $(".mu-selectbox .mu-value").click(function (e) {
    e.preventDefault();
    if ($(this).parent().hasClass("disabled") == true) {
      return false;
    }

    $(this).next(".mu-list").slideToggle("fast");
    $(this).parent().toggleClass("on");

    $(this)
      .next(".mu-list")
      .children("li")
      .click(function () {
        if ($(this).hasClass("disabled") == true) {
          //return false;
        } else if ($(this).children().hasClass("mu-checkbox") == true) {
          //return false;
          //$(this).parent().siblings(".mu-value").text($(this).children(".mu-checkbox").children("input:checked").siblings("label").text() + " 외 1개");
        } else {
          $(this).parent().siblings(".mu-value").text($(this).text());

          $(this).addClass("active").siblings().removeClass("active");
          $(this).parent().hide();
          $(this).parent().parent().removeClass("on");
        }
      });
  });

  /* selectbox 외에 영역 클릭시 */
  $("#wrap").click(function (e) {
    if (!$(".mu-selectbox").has(e.target).length) $(".mu-selectbox").find(".mu-list").hide();
  });

  var $floatingBtn = $(".floating-button");
  var $goTop = $(".floating-button .btn-go-top");
  // 스크롤시 상단으로 이동 버튼show
  $(window).scroll(function () {
    if ($(window).scrollTop() > 200) {
      $floatingBtn.addClass("show");
    } else {
      $floatingBtn.removeClass("show");
    }
  });
  // 로딩완료시 상단으로 이동 버튼show
  $(document).ready(function () {
    if ($(window).scrollTop() > 200) {
      $floatingBtn.addClass("show");
    }
  });
  // 맨위로가기
  $goTop.click(function (e) {
    e.preventDefault();
    $("html, body").stop().animate({ scrollTop: 0 }, 300);
  });

  var $tabTitle = $(".tab-area .mu-tab li a");

  $tabTitle.each(function() {
    $(this).on("click", function () {
      // index
      const num = $(this).parent().index();
      console.log("num : " + num);
      // class 삭제
      $(this).closest(".tab-area").find(".mu-tab li").removeClass("active");
      $(this).closest(".tab-area").find(".tab-content").removeClass("on");
  
      // class 추가
      $(this).closest(".tab-area").find(".mu-tab li:eq(" + num + ")").addClass("active");
      $(this).closest(".tab-area").find(".tab-content:eq(" + num + ")").addClass("on");
    });
  });
  })