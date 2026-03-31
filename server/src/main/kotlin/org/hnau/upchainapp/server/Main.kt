package org.hnau.upchainapp.server

import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import org.hnau.upchain.core.repository.file.upchains.fileBased
import org.hnau.upchain.core.repository.upchains.UpchainsRepository
import org.hnau.upchain.sync.core.ServerPort
import org.hnau.upchain.sync.server.core.ServerSyncApi
import org.hnau.upchain.sync.server.core.repository.toCreateOnly
import org.hnau.upchain.sync.server.http.httpSyncServer

fun main(
    args: Array<String>,
) {
    Logger.setLogWriters(platformLogWriter())

    val parser = ArgParser("Upchain server")
    val upchainsDir by parser.option(
        type = ArgType.String,
        shortName = "d",
    ).required()
    parser.parse(args)


    runBlocking {

        val repository = UpchainsRepository.fileBased(
            dir = upchainsDir,
        )

        val api = ServerSyncApi(
            scope = this,
            repository = repository.toCreateOnly(this),
        )

        httpSyncServer(
            api = api,
            port = ServerPort(8080),
        )
    }
}