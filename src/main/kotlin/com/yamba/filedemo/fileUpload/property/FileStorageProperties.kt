package com.yamba.filedemo.fileUpload.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
data class FileStorageProperties (var uploadDir: String)