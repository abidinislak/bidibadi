$(document).ready(function() {
		$("#fileImage").change(function() {

			showImageThumbnail(this);

			fileSize = this.files[0].size;

			if (fileSize > 102400) {
				this.setCustomValidity("100kb mb dan kucuk fotoyuklemelisiniz");
				this.reportValidity();
			}

			else {

			}
		});
	});

	function showImageThumbnail(fileInput) {

		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {

			$("#thumbnail").attr("src", e.target.result);
		};
		reader.readAsDataURL(file);

	}
	function showModalDialog(title, message) {
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();

	}
	
	function showErrorModal(message){
		
		showModalDialog("Error",message);
		
		
	}
	
	
	function showWarningModal(message){
		
		showModalDialog("Warning",message);
		
		
	}