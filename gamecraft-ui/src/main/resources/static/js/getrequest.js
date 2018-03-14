$( document ).ready(function() {
    
	var url = window.location;
	
	$("#btnId").click(function(event){
        event.preventDefault();
        // Open Bootstrap Modal
        openModel();
        // get data from Server
        ajaxGet();
	})
	
    // Open Bootstrap Modal
    function openModel(){
    	$("#modalId").modal();
    }
    
    // DO GET
    function ajaxGet(){
        $.ajax({
            type : "GET",
            url : url + "/greeting",
            success: function(data){
            	// fill data to Modal Body
                fillData(data);
            },
            error : function(e) {
            	fillData(null);
            }
        }); 
    }
    
    function fillData(data){
    	if(data!=null){
            $(".modal-body #greetingId").text(data);
    	}else{
            $(".modal-body #greetingId").text("Can Not Get Data from Server!");
    	}
    }
})