
 function cancelForm(){
       document.StakeHolder.action="showStakeHolderForm.htm";
       document.StakeHolder.submit();
}
 
 //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
       function stakeHolderDetail(stakeholderId) {	
		document.getElementById('stakeholderId').value=stakeholderId;
		submitlink('editStakeHolderForm.htm','StakeHolder');
	}
	//@..End