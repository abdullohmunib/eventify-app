/*! DataTables Tailwind CSS integration (Light Mode Only)
 *  Cleaned and simplified for full light theme consistency
 */
(function (factory) {
 if (typeof define === "function" && define.amd) {
  // AMD
  define(["jquery", "datatables.net"], function ($) {
   return factory($, window, document);
  });
 } else if (typeof exports === "object") {
  // CommonJS
  var jq = require("jquery");
  var cjsRequires = function (root, $) {
   if (!$.fn.dataTable) {
    require("datatables.net")(root, $);
   }
  };

  if (typeof window === "undefined") {
   module.exports = function (root, $) {
    if (!root) root = window;
    if (!$) $ = jq(root);
    cjsRequires(root, $);
    return factory($, root, root.document);
   };
  } else {
   cjsRequires(window, jq);
   module.exports = factory(jq, window, window.document);
  }
 } else {
  // Browser
  factory(jQuery, window, document);
 }
})(function ($, window, document) {
 "use strict";
 var DataTable = $.fn.dataTable;

 // Default renderer
 $.extend(true, DataTable.defaults, {
  renderer: "tailwindcss",
 });

 // Default class configuration (Light Mode Only)
 $.extend(true, DataTable.ext.classes, {
  container: "dt-container dt-tailwindcss text-sm",

  search: {
   input: "text-sm border placeholder-gray-500 ml-2 px-3 py-2 rounded-lg border-gray-200 focus:border-primary focus:ring focus:ring-primary focus:ring-opacity-50 bg-white text-gray-800",
  },

  length: {
   select: "text-sm border px-3 py-2 rounded-lg border-gray-200 focus:border-primary focus:ring focus:ring-primary focus:ring-opacity-50 bg-white text-gray-800",
  },

  processing: {
   container: "dt-processing text-sm",
  },

  paging: {
   active: "text-sm font-semibold bg-gray-100",
   notActive: "text-sm bg-white",
   button: "text-sm relative inline-flex justify-center items-center space-x-2 border px-4 py-2 -mr-px leading-6 hover:z-10 focus:z-10 active:z-10 border-gray-200 active:border-gray-200 active:shadow-none",
   first: "rounded-l-lg",
   last: "rounded-r-lg",
   enabled: "text-sm text-gray-800 hover:text-gray-900 hover:border-gray-300 hover:shadow-sm focus:ring focus:ring-gray-300 focus:ring-opacity-25",
   notEnabled: "text-sm text-gray-300",
  },

  table: "dataTable min-w-full text-sm align-middle whitespace-nowrap border border-gray-200 rounded-lg",

  thead: {
   row: "border-b border-gray-200",
   cell: "text-sm px-3 py-3 text-white bg-primary font-semibold text-left",
  },

  tbody: {
   row: "even:bg-gray-50",
   cell: "text-sm p-3 text-gray-800",
  },

  tfoot: {
   row: "even:bg-gray-50",
   cell: "text-sm p-3 text-left text-gray-800",
  },
 });

 // Paging Button Renderer
 DataTable.ext.renderer.pagingButton.tailwindcss = function (settings, buttonType, content, active, disabled) {
  var classes = settings.oClasses.paging;
  var btnClasses = [classes.button];

  btnClasses.push(active ? classes.active : classes.notActive);
  btnClasses.push(disabled ? classes.notEnabled : classes.enabled);

  var a = $("<a>", {
   href: disabled ? null : "#",
   class: btnClasses.join(" "),
  }).html(content);

  return {
   display: a,
   clicker: a,
  };
 };

 // Paging Container Renderer
 DataTable.ext.renderer.pagingContainer.tailwindcss = function (settings, buttonEls) {
  var classes = settings.oClasses.paging;
  buttonEls[0].addClass(classes.first);
  buttonEls[buttonEls.length - 1].addClass(classes.last);
  return $("<ul/>").addClass("pagination").append(buttonEls);
 };

 // Layout Renderer
 DataTable.ext.renderer.layout.tailwindcss = function (settings, container, items) {
  var row = $("<div/>", {
   class: items.full ? "grid grid-cols-1 gap-4 mb-4" : "grid grid-cols-2 gap-4 mb-4",
  }).appendTo(container);

  DataTable.ext.renderer.layout._forLayoutRow(items, function (key, val) {
   var klass;
   if (val.table) {
    klass = "col-span-2";
   } else if (key === "start") {
    klass = "justify-self-start";
   } else if (key === "end") {
    klass = "col-start-2 justify-self-end";
   } else {
    klass = "col-span-2 justify-self-center";
   }

   $("<div/>", {
    id: val.id || null,
    class: klass + " " + (val.className || ""),
   })
    .append(val.contents)
    .appendTo(row);
  });
 };

 return DataTable;
});
