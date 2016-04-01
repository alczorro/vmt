// hColumns by bu <bu@hax4.in>, BSD License

function hColumns(target,method) {
    var defaultConfig = {
        nodeSource: function() {
            return window.alert("dummy source, you need to create a node source");
        },

        noContentString: "没有成员",

        labelText_maxLength: 15,

        customNodeTypeIndicator: {},
		        customNodeTypeHandler: {}
    };
    
    var defaultHandler = {
        folder: function(hColumn, node, data) {
            hColumn.nodeSource( data.id, function(err, data) {
                if(err) {
                    return $.error(err);
                }

                return hColumn.columnView._addColumnList(data, hColumn.columnView);
            });
        },

        link: function(hColumn, node, data) {
            return window.open( data.url );
        }
    };

    var defaultIndicator = {
        folder: "icon-chevron-right",
        //edit by lvly @org=icon-global
        link: "icon-plus"
    };

    var methods = {
        init: function(options) {
            // extend the original default value with user input
            var settings = $.extend(defaultConfig , options);
            var handlers = $.extend(defaultHandler, settings.customNodeTypeHandler);
            var indicators = $.extend(defaultIndicator, settings.customNodeTypeIndicator);
            
            // return methods to the chain
            return this.each(function(){
                var self = $(this),
                data = self.data("columnView");
                // bind settings to columnview
                methods.settings = settings;

                // bind columnview and handler to the setings
                settings.columnView = methods;
                settings.handlers = handlers;
                settings.indicators = indicators;

                settings.container_node = this;
                
                // if this new html node is not activated hColumn
                if (!data) {
                    // we assign column view to our setting object
                    self.data("hColumn", settings);

                    // this container should add class for styling
                    self.addClass("column-view-container");
                    
                    // add a composition div inside
                    $("<div></div>").addClass("column-view-composition").appendTo(self);

                    // each node clicked should call this function
                    self.on("click", ".column ul li", settings.columnView._entryClick);
                    
                    // inital load
                    settings.nodeSource(null, function(err, data) {
                        if(err) {
                            return $.error(err);
                        }
                        if(data!=null&&data.length!=0){
                        	return settings.columnView._addColumnList(data);
                        }
                    });
                }
            });
        },

        _entryClick: function() {
            var columnView = $(this).parents(".column-view-container").data("hColumn");

            var current_container = $(this).parents(".column-view-container");

            var current_click_column = $(this).parents(".column");
            var current_click_level = $(this).parents(".column").index();

            var current_node_type = $(this).data("node-type");
            var current_node_data = $(this).data("node-data");
            // remove another subcolumns
            $(current_container).find(".column-view-composition .column:gt(" + current_click_level + ")").remove();
            // remove other active, and add clicked as active
            current_click_column.find(".active").removeClass("active");
            $(this).addClass("active");

            // redriect to different process model
            return columnView.handlers[current_node_type](columnView, this, current_node_data);
        },

        _addColumnList: function(list, columnView) {
            var self = (!columnView) ? this : columnView;
            var ListElm = $("<ul></ul>");
            if(list.length === 0) {
                var NoContentElm = $("<p></p>").text(columnView.settings.noContentString);

                return self._addColumn(NoContentElm, self);
            }
            //modify by lvly@ org  list.map(function(entry){} dosn't work in IE
            $.map(list,function(entry) {
            	
                // we create the element
            	var EntryElm = $("<li class='hcolumnLi' id=" + entry.id + "></li>").data("node-id", entry.id).data("node-type", entry.type).data("node-data", entry);
                var EntryElmSpan = $("<a data-pk='"+entry.dn+"' class='hcolumnLiSpan' title=\""+ document.createTextNode(entry.label).textContent + "\"></a>");
                var EntryIconElm = $("<i></i>").addClass( self.settings.indicators[entry.type] );
                var EntryTeam = $("<i></i>").addClass("icon-hdd");
                var EntryHoverPlus ='';
                var EntryHoverDelete = $('<i role="button"></i>').addClass("icon-trash");
                var EntryHoverEdit = $("<i></i>").addClass("icon-edit");
                if(entry.type=='link'){
                	EntryTeam = $("<i></i>").addClass("icon-user");
                }
                if(!entry.visible){
            		EntryTeam.addClass("hideNode");
            	}
               
               /* $(".column ul li i.icon-trash").live("click",function(){
                	$(this).parent().remove();
                })*/
                
                //-----------------------双击editable开始-----------------------
				/*$('.hcolumnLiSpan').editable({editBy : "dblclick",onSubmit:onEditName});*/
                var isAdmin=false;
                var editConfig={};
	           	if(self.settings.editable!=null){
	           		 editConfig=self.settings.editable;
	           		 isAdmin=self.settings.editable.isAdmin;
	           	 }
                $('li#'+ entry.id+' .icon-edit').die('click').live("click",function(event){
                	 event.stopPropagation();
                	 $(this).parent().children('a.hcolumnLiSpan').editable(editConfig);
                	 
                	/* attr("data-mode","inline");
                	 $(this).parent().children('a.hcolumnLiSpan').attr("data-toggle","manual");
                	 $(this).parent().children('a.hcolumnLiSpan').attr("data-showbuttons",false);*/
                	// $(this).parent().children('a.hcolumnLiSpan').editable('toggleDisabled');
                	 $(this).parent().children('a.hcolumnLiSpan').editable('toggle');
                	 
                })
				 
                // label cut string
				 if( entry.label.length > self.settings.labelText_maxLength ) {
                    entry.label = entry.label.substring(0, (self.settings.labelText_maxLength - 3) ) + "...";
                }
                
                // we build the node entry
                EntryElmSpan.append(document.createTextNode(entry.label) );
                EntryElm.append(EntryIconElm);
                EntryElm.append(EntryTeam);
                if(isAdmin){
                	EntryElm.append(EntryHoverDelete);
                	EntryElm.append(EntryHoverEdit);
                }
                EntryElm.append(EntryHoverPlus);
                EntryElm.append(EntryElmSpan);
                EntryElm.appendTo(ListElm);
            });

            return self._addColumn(ListElm, self);  
        },

        _addColumn: function(content_dom_node, columnView) {
            // create new column div
            var ColumnElm = $("<div></div>").addClass("column");

            // append the content
            ColumnElm.append(content_dom_node);

            // append this new coumn to the composition
            $(columnView.settings.container_node).find(".column-view-composition").append(ColumnElm);

            // scroll to the most right position (the place shows the latest click result)
            $(columnView.settings.container_node).scrollLeft( $(".column-view-composition").width() );
        }
    };   
    if ( methods[method] ) {
        return methods[ method ].apply( target, Array.prototype.slice.call( arguments, 1 ));
    } else if ( typeof method === 'object' || ! method ) {
        return methods.init.apply(target, [arguments[1]]);
    } else {
        $.error( 'Method ' +  method + ' does not exist on jQuery.hColumns' );
    }
}
