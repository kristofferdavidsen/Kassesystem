package com.pos.kasse.services

import com.pos.kasse.entities.Kategori
import com.pos.kasse.entities.Vare

interface ImplVareService {

    fun hentAlleVarer(): List<Vare>

    fun finnVareMedId(ean: Long): Vare

    fun hentVarerIKategori(enum: Enum<Kategori>, liste: List<Vare>): List<Vare>

    fun finnesVare(ean: Long): Boolean

    fun leggTilVare(vare: Vare)

    fun slettVare(vare: Vare)

    fun oppdaterVare(vare: Vare)

    fun leggTilAlleVarer(liste: MutableIterable<Vare>): MutableIterable<Vare>



}