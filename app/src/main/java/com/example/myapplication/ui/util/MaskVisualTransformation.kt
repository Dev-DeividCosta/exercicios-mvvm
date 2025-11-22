//package com.example.myapplication.ui.utils
//
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.input.OffsetMapping
//import androidx.compose.ui.text.input.TransformedText
//import androidx.compose.ui.text.input.VisualTransformation
//import kotlin.math.absoluteValue
//
///**
// * Transforma um texto puro em um texto com máscara visual.
// * Exemplo: Input "12345678900" + Mask "###.###.###-##" -> Visual "123.456.789-00"
// */
//class MaskVisualTransformation(val mask: String) : VisualTransformation {
//
//    private val specialChars = mask.filter { it != '#' }
//
//    override fun filter(text: AnnotatedString): TransformedText {
//        var out = ""
//        var maskIndex = 0
//        text.text.forEach { char ->
//            // CORREÇÃO AQUI: Usamos '?:' para garantir que não passamos null
//            // Se for null (fim da máscara), passamos '#' que faz o loop parar (pois # não está em specialChars)
//            while (specialChars.contains(mask.getOrNull(maskIndex) ?: '#')) {
//                out += mask[maskIndex]
//                maskIndex++
//            }
//            out += char
//            maskIndex++
//        }
//        return TransformedText(AnnotatedString(out), offsetMapping)
//    }
//
//    private val offsetMapping = object : OffsetMapping {
//        override fun originalToTransformed(offset: Int): Int {
//            val offsetValue = offset.absoluteValue
//            if (offsetValue == 0) return 0
//            var numberOfHashtags = 0
//            val masked = mask.takeWhile {
//                if (it == '#') numberOfHashtags++
//                numberOfHashtags < offsetValue
//            }
//            return masked.length + 1
//        }
//
//        override fun transformedToOriginal(offset: Int): Int {
//            return mask.take(offset.absoluteValue).count { it == '#' }
//        }
//    }
//}