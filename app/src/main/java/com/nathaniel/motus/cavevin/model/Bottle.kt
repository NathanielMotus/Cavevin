package com.nathaniel.motus.cavevin.model

import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.model.JsonObject
import com.nathaniel.motus.cavevin.model.CellComparator
import java.util.ArrayList

class Bottle {
    //**********************************************************************************************
    //**********************************************************************************************
    //Parameters
    //Couleur du vin : Rouge, Blanc ou Rosé (0, 1 ou 2)
    var type: String

    //Contenance de la bouteille
    /*
    Le piccolo : 20 cl (1/4 de bouteille)
    La chopine ou le quart : 25 cl (1/3 de bouteille)
    Le demi ou la fillette : 37,5 cl (1/2 bouteille)
    Le pot : 46cl (2/3 de bouteille)
    La bouteille : 75 cl
    Le magnum : 1.5 L (2 bouteilles)
    La Marie-Jeanne ou le double magnum : 3 L (4 bouteilles)
    Le réhoboam : 4.50 L (6 bouteilles)
    Le jéroboam : 5 L (presque 7 bouteilles)
    Le mathusalem ou l’impériale : 6 L (8 bouteilles)
    Le salmanazar : 9 L (12 bouteilles)
    Le balthazar : 12 L (16 bouteilles)
    Le nabuchodonosor : 15 L(20 bouteilles)
    Le melchior : 18 L (24 bouteilles)
    */
    var bottleName //nom de la bouteille
            : String
    var capacity //volume en litres
            : Double

    //Millésime
    var vintage: String

    //Appellation
    var appellation: String

    //Domaine
    var domain: String

    //Cuvée
    var cuvee: String

    //Commentaire : champ libre propre à la bouteille
    var bottleComment: String

    //Link to an image
    var photoName: String

    //**********************************************************************************************
    //Constructors
    //**********************************************************************************************
    constructor(isReferenced: Boolean) {
        //create a new bottle
        //if isReferenced is true, it is referenced in the BottleCatalog
        //if isReferenced is false, it is not referenced
        type = ""
        bottleName = ""
        capacity = 0.0
        vintage = ""
        appellation = ""
        domain = ""
        cuvee = ""
        bottleComment = ""
        photoName = ""
        if (isReferenced) {
            bottleCatalog.add(this)
        }
    }

    constructor(
        appellation: String,
        domain: String,
        cuvee: String,
        type: String,
        vintage: String,
        bottleName: String,
        capacity: Double,
        bottleComment: String,
        photoName: String,
        isReferenced: Boolean
    ) {
        //creates a complete bottle
        this.appellation = appellation
        this.domain = domain
        this.cuvee = cuvee
        this.type = type
        this.vintage = vintage
        this.bottleName = bottleName
        this.capacity = capacity
        this.bottleComment = bottleComment
        this.photoName = photoName
        if (isReferenced) {
            bottleCatalog.add(this)
        }
    }

    //**********************************************************************************************
    //Modifiers
    //**********************************************************************************************
    fun clearBottle() {
        //Clear all the fields of a bottle
        type = ""
        bottleName = ""
        capacity = 0.0
        vintage = ""
        appellation = ""
        domain = ""
        cuvee = ""
        bottleComment = ""
    }

    fun setBottleParametersOf(bottle: Bottle) {
        //copy the parameters of bottle to this
        appellation = bottle.appellation
        domain = bottle.domain
        cuvee = bottle.cuvee
        type = bottle.type
        vintage = bottle.vintage
        bottleName = bottle.bottleName
        capacity = bottle.capacity
        bottleComment = bottle.bottleComment
    }

    fun removeBottleFromCatalog() {
        //removes bottle from the bottle catalog
        //removes every use cases cells too
        //and finally removes these cells entries from the cellars
        while (findUseCaseCell(0) != null) findUseCaseCell(0)!!.removeCell()
        bottleCatalog.remove(this)
    }

    //**********************************************************************************************
    //Manipulators
    //**********************************************************************************************
    fun findUseCaseCell(fromIndex: Int): Cell? {
        //find the first use case cell of this from fromIndex
        return if (fromIndex >= Cell.Companion.cellPool.size) null else if (Cell.Companion.cellPool
                .get(fromIndex)
                .isBottleUseCase(this)
        ) Cell.Companion.cellPool
            .get(fromIndex) else findUseCaseCell(fromIndex + 1)
    }

    companion object {
        //**********************************************************************************************
        //Debug values
        private const val TAG = "Bottle"

        //**********************************************************************************************
        private const val SEPARATOR = "|"

        //**********************************************************************************************
        //The collection of bottles
        //Liste des références
        val bottleCatalog = ArrayList<Bottle>()

        //**********************************************************************************************
        //Getters and setters
        //**********************************************************************************************
        val numberOfReferences: Int
            get() = bottleCatalog.size

        fun clearBottleCatalog() {
            //empty bottle catalog
            bottleCatalog.clear()
        }
    }
}