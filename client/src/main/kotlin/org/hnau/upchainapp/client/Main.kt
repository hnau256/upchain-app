package org.hnau.upchainapp.client

import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.hnau.commons.kotlin.coroutines.createChild
import org.hnau.upchain.core.UpchainId
import org.hnau.upchain.core.repository.file.upchain.fileBased
import org.hnau.upchain.core.repository.upchain.UpchainRepository
import org.hnau.upchain.sync.client.core.sync
import org.hnau.upchain.sync.client.http.HttpSyncClient
import org.hnau.upchain.sync.core.ServerAddress
import org.hnau.upchain.sync.core.ServerPort
import org.hnau.upchain.sync.http.HttpScheme

private val logger = Logger.withTag("Main")

fun main(
    args: Array<String>,
) {
    Logger.setLogWriters(platformLogWriter())

    val parser = ArgParser("Upchain client")
    val upchainsFile by parser.option(
        type = ArgType.String,
        shortName = "f",
    ).required()
    val serverAddress by parser.option(
        type = ArgType.String,
        shortName = "s",
    ).required()
    parser.parse(args)

    runBlocking {

        val repository = UpchainRepository.fileBased(
            filename = upchainsFile,
        )

        val clientScope = createChild()
        val api = HttpSyncClient(
            scope = clientScope,
            address = ServerAddress(serverAddress),
            port = ServerPort(443),
            scheme = HttpScheme.Https,
        )

        repository
            .sync(
                id = upchainsFile.split("/").last().let(UpchainId.stringMapper.direct),
                api = api,
            )
            .getOrThrow()

        logger.i { "Synchronized" }

        clientScope.cancel()
    }
}