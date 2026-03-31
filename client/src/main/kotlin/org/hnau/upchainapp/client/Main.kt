package org.hnau.upchainapp.client

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import org.hnau.upchain.core.UpchainId
import org.hnau.upchain.core.repository.file.upchain.fileBased
import org.hnau.upchain.core.repository.upchain.UpchainRepository
import org.hnau.upchain.sync.client.core.sync
import org.hnau.upchain.sync.client.http.HttpSyncClient
import org.hnau.upchain.sync.core.ServerAddress
import org.hnau.upchain.sync.core.ServerPort

fun main(
    args: Array<String>,
) {

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

        val api = HttpSyncClient(
            scope = this,
            address = ServerAddress(serverAddress),
            port = ServerPort(8080),
        )

        repository
            .sync(
                id = upchainsFile.split("/").last().let(UpchainId.stringMapper.direct),
                api = api,
            )
            .getOrThrow()
    }
}