$(document).ready(function() {
		$("#fileImage").change(function() {

			showImageThumbnail(this);

			fileSize = this.files[0].size;

			if (fileSize > 1048576) {
				this.setCustomValidity("1 mb dan kucuk fotoyuklemelisiniz");
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