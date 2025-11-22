package com.example.myapplication.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue

/**
 * Uma transformação visual genérica que aceita uma máscara (ex: "###.###.###-##").
 * O caractere '#' representa onde o número deve entrar.
 */
class MaskVisualTransformation(val mask: String) : VisualTransformation {

    private val specialChars = mask.filter { it != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.text.forEach { char ->
            while (specialChars.contains(mask[maskIndex])) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }

    private val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            var offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}

/**
 * Transformação Específica para Telefone Brasileiro (8 ou 9 dígitos).
 * Alterna automaticamente entre (##) ####-#### e (##) #####-####
 */
class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trim = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        val mask = if (trim.length > 10) "(##) #####-####" else "(##) ####-####"

        val output = StringBuffer()
        for (i in trim.indices) {
            if (i == 0) output.append("(")
            if (i == 2) output.append(") ")

            // Lógica dinâmica para o traço
            val hyphenIndex = if (trim.length > 10) 7 else 6
            if (i == hyphenIndex) output.append("-")

            output.append(trim[i])
        }

        // Mapeamento do cursor (OffsetMapping simplificado para este caso específico)
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset <= 2) return offset + 1 // Conta o '('
                if (offset <= (if (trim.length > 10) 7 else 6)) return offset + 3 // Conta "() "
                if (offset <= 11) return offset + 4 // Conta "() -"
                return 15 // Fim
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset <= 2) return offset - 1
                if (offset <= (if (trim.length > 10) 10 else 9)) return offset - 3
                if (offset <= 15) return offset - 4
                return 11
            }
        }

        return TransformedText(AnnotatedString(output.toString()), numberOffsetTranslator)
    }
}