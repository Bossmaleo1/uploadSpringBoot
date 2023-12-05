package com.yamba.filedemo.fileUpload.controller

import com.yamba.filedemo.fileUpload.payload.UploadFileResponse
import com.yamba.filedemo.fileUpload.service.FileStorageService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors


@RestController
class FileController {
    @Autowired
    private val fileStorageService: FileStorageService? = null
    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): UploadFileResponse? {
        val fileName: String = fileStorageService!!.storeFile(file)
        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName)
            .toUriString()
        return file.contentType?.let {
            UploadFileResponse(
                fileName, fileDownloadUri,
                it, file.size
            )
        }
    }

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: Array<MultipartFile?>): MutableList<Any>? {
        return Arrays.asList<MultipartFile>(*files)
            .stream()
            .map<Any>(Function<MultipartFile, Any> { file: MultipartFile -> uploadFile(file) })
            .collect(Collectors.toList<Any>())
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String?, request: HttpServletRequest): ResponseEntity<Resource> {
        // Load file as Resource
        val resource: Resource? = fileName?.let { fileStorageService!!.loadFileAsResource(it) }

        // Try to determine file's content type
        var contentType: String? = null
        try {
            contentType = request.servletContext.getMimeType(resource?.file?.absolutePath)
        } catch (ex: IOException) {
            logger.info("Could not determine file type.")
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource?.filename + "\"")
            .body(resource)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileController::class.java)
    }
}