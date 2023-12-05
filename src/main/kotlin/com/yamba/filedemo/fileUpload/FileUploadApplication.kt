package com.yamba.filedemo.fileUpload

import com.yamba.filedemo.fileUpload.property.FileStorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties::class)
class FileUploadApplication

fun main(args: Array<String>) {
	runApplication<FileUploadApplication>(*args)
}
