/**
 * @license Copyright (c) 2014-2024, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */

import { ClassicEditor } from '@ckeditor/ckeditor5-editor-classic';

import { Autoformat } from '@ckeditor/ckeditor5-autoformat';
import { Bold, Code, Italic } from '@ckeditor/ckeditor5-basic-styles';
import { BlockQuote } from '@ckeditor/ckeditor5-block-quote';
import { CodeBlock } from '@ckeditor/ckeditor5-code-block';
import type { EditorConfig } from '@ckeditor/ckeditor5-core';
import { Essentials } from '@ckeditor/ckeditor5-essentials';
import { FontColor, FontFamily, FontSize, FontBackgroundColor } from '@ckeditor/ckeditor5-font';
import { Heading } from '@ckeditor/ckeditor5-heading';
import {
	Image,
	ImageCaption,
	ImageStyle,
	ImageToolbar,
	ImageUpload,
  ImageResize,
  ImageBlock,
  ImageInline,
  ImageInsertViaUrl
} from '@ckeditor/ckeditor5-image';
import { Indent } from '@ckeditor/ckeditor5-indent';
import { Alignment } from '@ckeditor/ckeditor5-alignment';
import { Link } from '@ckeditor/ckeditor5-link';
import { List } from '@ckeditor/ckeditor5-list';
import { MediaEmbed } from '@ckeditor/ckeditor5-media-embed';
import { Paragraph } from '@ckeditor/ckeditor5-paragraph';
import { PasteFromOffice } from '@ckeditor/ckeditor5-paste-from-office';
import { Table, TableToolbar } from '@ckeditor/ckeditor5-table';
import { TextTransformation } from '@ckeditor/ckeditor5-typing';
import { Undo } from '@ckeditor/ckeditor5-undo';
import { Base64UploadAdapter, SimpleUploadAdapter } from '@ckeditor/ckeditor5-upload';

// You can read more about extending the build with additional plugins in the "Installing plugins" guide.
// See https://ckeditor.com/docs/ckeditor5/latest/installation/plugins/installing-plugins.html for details.
const isLocal = (): boolean => {
  const isLocalhost = location.hostname === "localhost";
  return isLocalhost;
};
class Editor extends ClassicEditor {
	public static override builtinPlugins = [
		Autoformat,
		BlockQuote,
		Bold,
		Code,
		CodeBlock,
		Essentials,
		FontColor,
    FontBackgroundColor,
		FontFamily,
		FontSize,
    Alignment,
		Heading,
		Image,
		ImageCaption,
		ImageStyle,
		ImageToolbar,
		ImageUpload,
    ImageResize,
    ImageBlock,
    ImageInline,
    ImageInsertViaUrl,
		Indent,
		Italic,
		Link,
		List,
		MediaEmbed,
		Paragraph,
		PasteFromOffice,
		Table,
		TableToolbar,
		TextTransformation,
		Undo,
    // Base64UploadAdapter,
    SimpleUploadAdapter
	];

	public static override defaultConfig: EditorConfig = {
		toolbar: {
			items: [
				'heading',
				'|',
				'bold',
				'italic',
				'link',
				'bulletedList',
				'numberedList',
				'fontColor',
				'fontBackgroundColor',
				'fontFamily',
				'fontSize',
				'|',
        'alignment',
        '|',
				'outdent',
				'indent',
				'|',
        'insertImage',
				'blockQuote',
				'insertTable',
				'code',
				'codeBlock',
        '|',
				'undo',
				'redo'
			]
		},
		image: {
			toolbar: [
				'imageTextAlternative',
				'toggleImageCaption',
        'imageStyle:inline', // 인라인 스타일
				'imageStyle:alignLeft',
				'imageStyle:alignCenter',
				'imageStyle:alignRight',
			],
      resizeUnit: 'px',
		},
		table: {
			contentToolbar: [
				'tableColumn',
				'tableRow',
				'mergeTableCells'
			]
		},
    simpleUpload: {
      uploadUrl: isLocal()
      ? "http://localhost:28080/api/ckeditor/attachFile"
      : "/api/ckeditor/attachFile",
      withCredentials: true,
      headers: {
        'X-CSRF-TOKEN': 'CSRF-Token',
        'Authorization': 'Bearer <JSON Web Token>'
      }
    },
    fontSize: {
      options: [
        11,
        13,
        'default',
        15,
        16,
        19,
        24,
        28,
        30,
        34,
      ],
      supportAllValues: true
    },
    fontFamily: {
      options: [
          'default',
          'Arial, Helvetica, sans-serif',
          'Courier New, Courier, monospace',
          'Georgia, serif',
          'Lucida Sans Unicode, Lucida Grande, sans-serif',
          'Tahoma, Geneva, sans-serif',
          'Times New Roman, Times, serif',
          'Trebuchet MS, Helvetica, sans-serif',
          'Verdana, Geneva, sans-serif',
      ],
      supportAllValues: true // 사용자가 지정하는 모든 폰트 지원
  },
	};
}

export default Editor;
