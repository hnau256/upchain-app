package org.hnau.upchainapp.server

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import org.hnau.upchain.core.repository.file.upchains.fileBased
import org.hnau.upchain.core.repository.upchains.UpchainsRepository
import org.hnau.upchain.sync.core.ServerPort
import org.hnau.upchain.sync.server.repository.toCreateOnly
import org.hnau.upchain.sync.server.tcpSyncServer

fun main(
    args: Array<String>,
) {

    val parser = ArgParser("Upchain server")
    val port by parser.option(
        type = ArgType.Int,
        shortName = "p",
    )
    val upchainsDir by parser.option(
        type = ArgType.String,
        shortName = "d",
    ).required()
    parser.parse(args)


    runBlocking {

        val repository = UpchainsRepository.fileBased(
            dir = upchainsDir,
        )

        tcpSyncServer(
            repository = repository.toCreateOnly(this),
            port = port?.let(::ServerPort) ?: ServerPort.default,
        )
    }
}