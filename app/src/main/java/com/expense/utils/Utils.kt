package com.expense.utils

import android.content.res.AssetManager

/**
 * Created by Jasvir Partap Singh on 21,May,2022
 */

 fun AssetManager.readAssetsFile(s: String): String {
    return open(s).bufferedReader().use { it.readText() }
}