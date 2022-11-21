package com.ivancaas.beersapp.data.remote


import com.google.gson.annotations.SerializedName

class BeersResponse : ArrayList<BeersResponse.BeersResponseItem>() {
    data class BeersResponseItem(
        val abv: Double,
        @SerializedName("attenuation_level")
        val attenuationLevel: Double,
        @SerializedName("boil_volume")
        val boilVolume: BoilVolume,
        @SerializedName("brewers_tips")
        val brewersTips: String,
        @SerializedName("contributed_by")
        val contributedBy: String,
        val description: String,
        val ebc: Double?,
        @SerializedName("first_brewed")
        val firstBrewed: String,
        @SerializedName("food_pairing")
        val foodPairing: List<String>,
        val ibu: Double?,
        val id: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        val ingredients: Ingredients,
        val method: Method,
        val name: String,
        val ph: Double?,
        val srm: Double?,
        val tagline: String,
        @SerializedName("target_fg")
        val targetFg: Int,
        @SerializedName("target_og")
        val targetOg: Double,
        val volume: Volume
    ) {

        data class BoilVolume(
            val unit: String,
            val value: Int
        )

        data class Ingredients(
            val hops: List<Hop>,
            val malt: List<Malt>,
            val yeast: String
        ) {
            data class Hop(
                val add: String,
                val amount: Amount,
                val attribute: String,
                val name: String
            ) {
                data class Amount(
                    val unit: String,
                    val value: Double
                )
            }

            data class Malt(
                val amount: Amount,
                val name: String
            ) {
                data class Amount(
                    val unit: String,
                    val value: Double
                )
            }
        }

        data class Method(
            val fermentation: Fermentation,
            @SerializedName("mash_temp")
            val mashTemp: List<MashTemp>,
            val twist: String?
        ) {
            data class Fermentation(
                val temp: Temp
            ) {
                data class Temp(
                    val unit: String,
                    val value: Int
                )
            }

            data class MashTemp(
                val duration: Int?,
                val temp: Temp
            ) {
                data class Temp(
                    val unit: String,
                    val value: Int
                )
            }
        }

        data class Volume(
            val unit: String,
            val value: Int
        )
    }
}