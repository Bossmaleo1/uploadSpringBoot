package com.yamba.filedemo.fileUpload.payload


class UploadFileResponse(var fileName: String, var fileDownloadUri: String, var fileType: String, var size: Long)