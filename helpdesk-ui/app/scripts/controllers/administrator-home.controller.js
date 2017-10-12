/**
 * Administrator Controller
 */

(function() {
    'use strict';
    angular.module('application')
        .controller('AdministratorController', AdministratorController);

    AdministratorController.$inject = ['$scope', '$state', '$cookies', 'RequestFactory'];

    function AdministratorController($scope, $state, $cookies, RequestFactory) {
        RequestFactory.isAlreadyAuthenticated();

        $(document).ready(function() {

            if (typeof jQuery === "undefined") {
                throw new Error("AdminLTE requires jQuery");
            }

            $.AdminLTE = {};

            $.AdminLTE.options = {
                navbarMenuSlimscroll: true,
                navbarMenuSlimscrollWidth: "3px",
                navbarMenuHeight: "200px",
                animationSpeed: 500,
                sidebarToggleSelector: "[data-toggle='offcanvas']",
                sidebarPushMenu: true,
                sidebarSlimScroll: true,
                sidebarExpandOnHover: false,
                enableBoxRefresh: true,
                enableBSToppltip: true,
                BSTooltipSelector: "[data-toggle='tooltip']",
                enableFastclick: false,
                enableControlSidebar: true,
                controlSidebarOptions: {
                    toggleBtnSelector: "[data-toggle='control-sidebar']",
                    selector: ".control-sidebar",
                    slide: true
                },
                colors: {
                    lightBlue: "#3c8dbc",
                    red: "#f56954",
                    green: "#00a65a",
                    aqua: "#00c0ef",
                    yellow: "#f39c12",
                    blue: "#0073b7",
                    navy: "#001F3F",
                    teal: "#39CCCC",
                    olive: "#3D9970",
                    lime: "#01FF70",
                    orange: "#FF851B",
                    fuchsia: "#F012BE",
                    purple: "#8E24AA",
                    maroon: "#D81B60",
                    black: "#222222",
                    gray: "#d2d6de"
                },
                screenSizes: {
                    xs: 480,
                    sm: 768,
                    md: 992,
                    lg: 1200
                }
            };

            /* ------------------
             * - Implementation -
             * ------------------
             * The next block of code implements AdminLTE's
             * functions and plugins as specified by the
             * options above.
             */
            $(function() {
                "use strict";

                //Fix for IE page transitions
                $("body").removeClass("hold-transition");

                //Extend options if external options exist
                if (typeof AdminLTEOptions !== "undefined") {
                    $.extend(true,
                        $.AdminLTE.options,
                        AdminLTEOptions);
                }

                //Easy access to options
                var o = $.AdminLTE.options;

                //Set up the object
                _init();

                //Activate the layout maker
                $.AdminLTE.layout.activate();

                //Enable sidebar tree view controls
                $.AdminLTE.tree('.sidebar');

                //Add slimscroll to navbar dropdown
                if (o.navbarMenuSlimscroll && typeof $.fn.slimscroll != 'undefined') {
                    $(".navbar .menu").slimscroll({
                        height: o.navbarMenuHeight,
                        alwaysVisible: false,
                        size: o.navbarMenuSlimscrollWidth
                    }).css("width", "100%");
                }

                //Activate fast click
                if (o.enableFastclick && typeof FastClick != 'undefined') {
                    FastClick.attach(document.body);
                }

                /*
                 * INITIALIZE BUTTON TOGGLE
                 * ------------------------
                 */
                $('.btn-group[data-toggle="btn-toggle"]').each(function() {
                    var group = $(this);
                    $(this).find(".btn").on('click', function(e) {
                        group.find(".btn.active").removeClass("active");
                        $(this).addClass("active");
                        e.preventDefault();
                    });

                });
            });

            /* ----------------------------------
             * - Initialize the AdminLTE Object -
             * ----------------------------------
             * All AdminLTE functions are implemented below.
             */
            function _init() {
                'use strict';
                /* Layout
                 * ======
                 * Fixes the layout height in case min-height fails.
                 *
                 * @type Object
                 * @usage $.AdminLTE.layout.activate()
                 *        $.AdminLTE.layout.fix()
                 *        $.AdminLTE.layout.fixSidebar()
                 */
                $.AdminLTE.layout = {
                    activate: function() {
                        var _this = this;
                        _this.fix();
                        _this.fixSidebar();
                        $(window, ".wrapper").resize(function() {
                            _this.fix();
                            _this.fixSidebar();
                        });
                    },
                    fix: function() {
                        //Get window height and the wrapper height
                        var neg = $('.main-header').outerHeight() + $('.main-footer').outerHeight();
                        var window_height = $(window).height();
                        var sidebar_height = $(".sidebar").height();
                        //Set the min-height of the content and sidebar based on the
                        //the height of the document.
                        if ($("body").hasClass("fixed")) {
                            $(".content-wrapper, .right-side").css('min-height', window_height - $('.main-footer').outerHeight());
                        } else {
                            var postSetWidth;
                            if (window_height >= sidebar_height) {
                                $(".content-wrapper, .right-side").css('min-height', window_height - neg);
                                postSetWidth = window_height - neg;
                            } else {
                                $(".content-wrapper, .right-side").css('min-height', sidebar_height);
                                $(".admin-dash").css("height", sidebar_height);
                                postSetWidth = sidebar_height;
                            }

                            //Fix for the control sidebar height
                            var controlSidebar = $($.AdminLTE.options.controlSidebarOptions.selector);
                            if (typeof controlSidebar !== "undefined") {
                                if (controlSidebar.height() > postSetWidth)
                                    $(".content-wrapper, .right-side").css('min-height', controlSidebar.height());
                            }

                        }
                    },
                    fixSidebar: function() {
                        //Make sure the body tag has the .fixed class
                        if (!$("body").hasClass("fixed")) {
                            if (typeof $.fn.slimScroll != 'undefined') {
                                $(".sidebar").slimScroll({ destroy: true }).height("auto");
                            }
                            return;
                        } else if (typeof $.fn.slimScroll == 'undefined' && window.console) {
                            window.console.error("Error: the fixed layout requires the slimscroll plugin!");
                        }
                        //Enable slimscroll for fixed layout
                        if ($.AdminLTE.options.sidebarSlimScroll) {
                            if (typeof $.fn.slimScroll != 'undefined') {
                                //Destroy if it exists
                                $(".sidebar").slimScroll({ destroy: true }).height("auto");
                                //Add slimscroll
                                $(".sidebar").slimscroll({
                                    height: ($(window).height() - $(".main-header").height()) + "px",
                                    color: "rgba(0,0,0,0.2)",
                                    size: "3px"
                                });
                            }
                        }
                    }
                };

                /* Tree()
                 * ======
                 * Converts the sidebar into a multilevel
                 * tree view menu.
                 *
                 * @type Function
                 * @Usage: $.AdminLTE.tree('.sidebar')
                 */
                $.AdminLTE.tree = function(menu) {
                    var _this = this;
                    var animationSpeed = $.AdminLTE.options.animationSpeed;
                    $(document).off('click', menu + ' li a')
                        .on('click', menu + ' li a', function(e) {
                            //Get the clicked link and the next element
                            var $this = $(this);
                            var checkElement = $this.next();

                            //Check if the next element is a menu and is visible
                            if ((checkElement.is('.treeview-menu')) && (checkElement.is(':visible')) && (!$('body').hasClass('sidebar-collapse'))) {
                                //Close the menu
                                checkElement.slideUp(animationSpeed, function() {
                                    checkElement.removeClass('menu-open');
                                    //Fix the layout in case the sidebar stretches over the height of the window
                                    //_this.layout.fix();
                                });
                                checkElement.parent("li").removeClass("active");
                                $(".admin-dash").css("height", "100vh");
                                $(".content-wrapper, .right-side").css("min-height", "92.5vh");
                            }
                            //If the menu is not visible
                            else if ((checkElement.is('.treeview-menu')) && (!checkElement.is(':visible'))) {
                                //Get the parent menu
                                var parent = $this.parents('ul').first();
                                //Close all open menus within the parent
                                var ul = parent.find('ul:visible').slideUp(animationSpeed);
                                //Remove the menu-open class from the parent
                                ul.removeClass('menu-open');
                                //Get the parent li
                                var parent_li = $this.parent("li");

                                //Open the target menu and add the menu-open class
                                checkElement.slideDown(animationSpeed, function() {
                                    //Add the class active to the parent li
                                    checkElement.addClass('menu-open');
                                    parent.find('li.active').removeClass('active');
                                    parent_li.addClass('active');
                                    //Fix the layout in case the sidebar stretches over the height of the window
                                    _this.layout.fix();
                                });
                            }
                            //if this isn't a link, prevent the page from being redirected
                            if (checkElement.is('.treeview-menu')) {
                                e.preventDefault();
                            }
                        });
                };

            }
        });

        $scope.pushMenu = pushMenu;
        $scope.logout = logout;

        // call push menu for first time when page reload
        // pushMenu();

        /**--------------------------------------- */

        function pushMenu() {
            if ($("body").hasClass('sidebar-collapse')) {
                $("body").removeClass('sidebar-collapse').trigger('expanded.pushMenu');
            } else {
                $("body").addClass('sidebar-collapse').trigger('collapsed.pushMenu');
                $(".content-wrapper").css("min-height", "92.5vh");
                $(".admin-dash").css("height", "100vh");
            }
        }

        function logout() {
            RequestFactory.logout().then(
                function(response) {
                    $cookies.remove("loggingIn");
                    $state.go("login");
                },
                function(error) {
                    console.log("Error : " + error);
                });
        }

    };
})();