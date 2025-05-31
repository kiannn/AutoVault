<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/css/bootstrap.min.css"  rel="stylesheet">
        <link href="/css/bootstrap-datepicker.standalone.min.css" rel="stylesheet">
        <style>
            .customT {
              border:none;
              position: relative;
              left: 25px;
              margin-top: 30px;
              margin-bottom: 10px;
            }
        
            .customT td {
                  padding-bottom: 2px;
                  padding-top: 2px;
                  padding-left: 1px;
                  padding-right: 5px;
                  border: none;
                }
                
            .relative {
                /*position: relative;
                left: 193px;*/
                border: 3px dotted wheat;
                margin-top: 20px; 
                width: 65%;
            }
            .header {
                position:relative; 
                left: 27px; 
                top: 20px; 
                border-bottom: 2px solid darkslategray;
                width: 655px;
                padding-bottom: 5px;
            }

        </style>
        <title>Add Or Update Page</title>
    </head>
    <body style=" background-image: url('/images/plain_white_wallpaper.jpg');">
        <%@include file="jspfs/navigationBar.jspf"%>
        <div class="container  mb-5 alert-link">
            <c:if test="${message.contains('Edit') && docdelet!=null}">
                <div style="justify-content: center; display: flex; margin-bottom: 45px">               
                    <!---------------------------------------------------------- 
                    Old approach, when 'docdelet' was included in request parameter:
                        <span style="position: absolute; height: 40px;" id="msg" class="alert alert-warning pt-2">${param.docdelet}</span> 
                    ------------------------------------------------------------>        
                    <span style="position: absolute; height: 40px;" id="msg" class="alert alert-warning pt-2">${docdelet}</span>
                </div>
            </c:if>  
            <p id="pageHeade" style="color:black; font-weight: bolder; font-size: 20px;text-align: center; margin-top: 40px">${message}</p>
            <p style="color:darkcyan; font-size: 16px; text-align: center;">
                Enter vehicle details and press ${button} button to ${button.toLowerCase()} it
            </p>

            <%@include file="jspfs/form.jspf"%>
        </div>            
            
        <%@include file="jspfs/bootstrapJS_jqueryJS.jspf"%>
        <script src="/js/bootstrap-datepicker.min.js"></script>
    </body>
</html>

<script type="text/javascript">

    $('#datePurchased').datepicker({
        format: 'yyyy-M-dd'

    });
   
    function blank(){

    const form = document.getElementById('fo');
    const elements = form.elements;

    for (let i = 0; i < elements.length; i++) {

                const field = elements[i]; 

                if(field.value ==='Cancel' ||  
                   field.value === 'Add'   || 
                   field.value === 'Update'||
                   field.value === 'Clear'||
                   field.value==='Upload more file' ||
                   field.value === 'remove'){ 

                    continue;
                }

                field.value = ''; 
            }    
        }
       
    function addOrdelete(){
        
        const table = document.getElementById("tableAdd");
                
        if(table.rows.length<5){
            
            var newrow = table.insertRow(-1);
            var cell1 = newrow.insertCell(0);
            cell1.style="text-align: right;";
            var cell2 = newrow.insertCell(1);
            var cell3 = newrow.insertCell(2);

            //var row_Indx = newrow.rowIndex; // get the index of newly added row in the table 
            
            var newRemoveBut = document.createElement("input");
            newRemoveBut.type="button";
            newRemoveBut.value="remove";
            newRemoveBut.className="btn btn-danger text-black alert-link p-1";
            newRemoveBut.onclick=function (){         
               table.deleteRow(this.parentNode.parentNode.rowIndex);
            };
            
            var newUploaBut = document.createElement("input");
            newUploaBut.type="file";
            newUploaBut.name="filee";
            newUploaBut.className="btn btn-info";
            newUploaBut.style="width:280px;";
            newUploaBut.oninput=function (){checkFileSize(this, "tableAdd");};
            
            var msgExceed = document.createElement("div");
           /* Old approach - we assigned an 'id' to each 'div' within the third 
            *                cell of each row derived from the current row id : msgExceed.id = "msgExceed"+row_Indx;
            * This approach did not work as whenever a row is deleted the rest of the rows indices
            * would be inconsistent with those rows corresponding <div> ids. 
            * If there are three rows with indices 0, 1, 2 and corresponding <div> ids msgExceed0, msgExceed1, msgExceed2
            * and if row index 1 is deleted, then the corresponding <div> of row index 1 would have an id of msgExceed2            
           */
            msgExceed.className="mb-2";
            msgExceed.setAttribute("name","largeUploadError"); // msgExceed.name="largeUploadError"; will not work for a <div>
            msgExceed.style="color:red;";
            
            cell1.appendChild(newRemoveBut);
            cell2.appendChild(newUploaBut);
            cell3.appendChild(msgExceed);
        }

    }
 
    function checkFileSize(input, tableID) {
        
          const maxSizeInBytes = 5.5 * 1024 * 1024;
          const file = input.files[0];
          const rowIndx = input.parentNode.parentNode.rowIndex; 
          
          var table = document.getElementById(tableID);
          var division = table.rows[rowIndx].cells[table.rows[rowIndx].cells.length-1].querySelector('div[name = "largeUploadError"]');

          if (file.size > maxSizeInBytes) { 
              modifyDiv(division, input, 'Maximum upload size exceeded');    
              
          } else{     
              
            let inputV = input.value;
            inputV = inputV.substring(inputV.lastIndexOf('.'));
            inputV = inputV.concat(","); // inputV must starts with '.' and ends with ','
            
            const a = '${listOfAcceptedfileExts}';
    
                 if(!a.includes(inputV)){
                     
                    let msg = inputV.includes('fakepath')? "Invalid file":
                              "'"+inputV.substring(1,inputV.length-1)+"' is invalid format";
                    
                    modifyDiv(division, input,msg);
                }
         }
          
    }
    
    function modifyDiv(division,input,  msg) {
    
        division.style.display = "block"; // division.style.display = "block" must be set in order to get 'division' visible for the next invocation
        input.value='';
        //Old approach : var divv = document.getElementById("msgExceed"+indx); 
        division.innerHTML = msg; // could also be divv.textContent
        setTimeout(function(){ division.style.display = "none"; }, 3000);    

    }

    setTimeout(function(){ (document.getElementById("msg")).style.display = "none"; }, 3000); 

   
</script> 
