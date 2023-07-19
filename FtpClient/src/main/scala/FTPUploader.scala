package com.yuanta.sre.ftp

import org.apache.commons.net.ftp.{FTP, FTPClient}
import scopt.OParser

import java.io.{File, FileInputStream, IOException}
import scala.sys.exit

case class Config(
                   server: String = "",
                   port: Int = 21,
                   useraccount: String = "",
                   password: String = "",
                   localFilePath: String = "",
                   remoteFilePath: String = ""
                 )

object FTPUploader {
  val builder = OParser.builder[Config]
  val argParser = {
    import builder._
    OParser.sequence(
      programName("ftpClient"),
      head("ftpClient", "0.1"),
      opt[String]('s', "server")
        .required()
        .action((s, c) => c.copy(server = s))
        .text("required server address")
        .validate(s => {
          if (s.nonEmpty) {
            success
          } else {
            failure("server address is empty")
          }
        }),
      opt[Int]('p', "port")
        .action((p, c) => c.copy(port = p)),
      opt[String]('u', "useraccount")
        .required()
        .text("required useraccount")
        .action((u, c) => c.copy(useraccount = u))
        .validate(s => {
          if (s.nonEmpty) {
            success
          } else {
            failure("useraccount is empty")
          }
        }),
      opt[String]("password")
        .required()
        .text("required password")
        .action((p, c) => c.copy(password = p))
        .validate(s => {
          if (s.nonEmpty) {
            success
          } else {
            failure("password is empty")
          }
        }),
      opt[String]("local")
        .required()
        .text("required local file path")
        .action((p, c) => c.copy(localFilePath = p))
        .validate(s => {
          if (s.nonEmpty) {
            success
          } else {
            failure("local file path is empty")
          }
        }),
      opt[String]("remote")
        .required()
        .text("required remote file path")
        .action((p, c) => c.copy(remoteFilePath = p))
        .validate(s => {
          if (s.nonEmpty) {
            success
          } else {
            failure("remote file path is empty")
          }
        }))
  }

  def uploadFile(server: String, port: Int, useraccount: String, password: String,
                 localFilePath: String, remoteFilePath: String): Unit = {
    val ftpClient = new FTPClient
    var fis: FileInputStream = null

    try {
      ftpClient.connect(server, port)
      ftpClient.login(useraccount, password)

      ftpClient.enterLocalPassiveMode()
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

      val localFile = new File(localFilePath)
      fis = new FileInputStream(localFile)

      val remoteFile = remoteFilePath + "/" + localFile.getName
      ftpClient.storeFile(remoteFile, fis)

      println(s"檔案上傳成功: ${localFile.getAbsolutePath} -> $remoteFile")
    } catch {
      case e: IOException => e.printStackTrace()
    } finally {
      try {
        if (fis != null) {
          fis.close()
        }
        ftpClient.logout()
        ftpClient.disconnect()
      } catch {
        case e: IOException => e.printStackTrace()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    // 請替換以下val builder = OParser.builder[Config]
      OParser.parse(argParser, args, Config()) match {
        case Some(config) =>
          println(OParser.usage(argParser))
          uploadFile(config.server, config.port, config.useraccount, config.password, config.localFilePath, config.remoteFilePath)
        // do stuff with config
        case _ =>
          exit(1)
      }


    }
  }

