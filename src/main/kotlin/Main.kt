import kotlin.system.exitProcess

// Klasse, um ein einzelnes Menüelement zu repräsentieren
class MenuItem(val name: String, val price: Double)

// Klasse, die das Menü verwaltet und die Menüelemente speichert
class Menu {
    val items: MutableList<MenuItem> = mutableListOf()

    // Methode, um ein Menüelement zum Menü hinzuzufügen
    fun addItem(item: MenuItem) {
        items.add(item)
    }

    // Methode, um das gesamte Menü anzuzeigen
    fun displayMenu() {
        println("Menu:")
        for ((index, item) in items.withIndex()) {
            println("${index + 1}. ${item.name} - ${item.price}")
        }
    }
}

// Klasse, die einen einzelnen Tisch repräsentiert und die Bestellungen für diesen Tisch speichert
class Table(val number: Int) {
    val orders: MutableList<MenuItem> = mutableListOf()

    // Methode, um ein Menüelement zur Bestellung des Tisches hinzuzufügen
    fun addToOrder(item: MenuItem) {
        orders.add(item)
    }

    // Methode, um ein Menüelement aus der Bestellung des Tisches zu entfernen
    fun removeFromOrder(item: MenuItem) {
        orders.remove(item)
    }

    // Methode, um die Bestellung des Tisches anzuzeigen
    fun displayOrder() {
        println("Table $number Order:")
        for ((index, item) in orders.withIndex()) {
            println("${index + 1}. ${item.name} - ${item.price}")
        }
    }

    // Methode, um die Gesamtsumme der Bestellung des Tisches zu berechnen
    fun calculateTotal(): Double {
        var total = 0.0
        for (item in orders) {
            total += item.price
        }
        return total
    }
}

// Klasse, die den Kassierer repräsentiert und die Interaktion mit den Tischen und dem Menü verwaltet
class Cashier(val menu: Menu) {
    private val tables: MutableMap<Int, Table> = mutableMapOf()

    // Methode, um einen Tisch zu öffnen und Bestellungen für diesen Tisch aufzunehmen
    fun openTable(tableNumber: Int) {
        if (tables.containsKey(tableNumber)) {
            println("Table $tableNumber is already open.")
        } else {
            tables[tableNumber] = Table(tableNumber)
            println("Table $tableNumber is now open.")

            val table = tables[tableNumber]
            if (table != null) {
                println("Menu:")
                menu.displayMenu()

                while (true) {
                    print("Enter the item number (0 to finish ordering): ")
                    val itemNumber = readLine()?.toIntOrNull()

                    if (itemNumber == 0) {
                        break
                    }

                    val item = menu.items.getOrNull(itemNumber?.minus(1) ?: -1)
                    if (item != null) {
                        table.addToOrder(item)
                        println("${item.name} added to the order.")
                    } else {
                        println("Invalid item number.")
                    }
                }

                println("Order for Table $tableNumber:")
                table.displayOrder()
                println("Table $tableNumber is ready to be served.")
            }
        }
    }

    // Methode, um einen Tisch zu schließen und die Gesamtsumme der Bestellung anzuzeigen
    fun closeTable(tableNumber: Int) {
        val table = tables[tableNumber]
        if (table != null) {
            table.displayOrder()
            val total = table.calculateTotal()
            println("Total for Table $tableNumber: $total")
            tables.remove(tableNumber)
            println("Table $tableNumber is now closed.")
        } else {
            println("Table $tableNumber is not open.")
        }
    }

    // Methode, um alle geöffneten Tische anzuzeigen und in einen bestimmten Tisch einzusteigen, um weitere Bestellungen hinzuzufügen oder zu entfernen
    fun displayAllTables() {
        if (tables.isEmpty()) {
            println("No tables are currently open.")
        } else {
            println("Open Tables:")
            for ((_, table) in tables) {
                println("Table ${table.number}")
                table.displayOrder()
                val total = table.calculateTotal()
                println("Total: $total")
                println()
            }

            print("Enter the table number to go inside (0 to exit): ")
            val tableNumber = readLine()?.toIntOrNull()

            if (tableNumber != null && tableNumber != 0) {
                val table = tables[tableNumber]
                if (table != null) {
                    println("Table ${table.number} Order:")
                    table.displayOrder()

                    while (true) {
                        println("1. Add Item")
                        println("2. Remove Item")
                        println("3. Back to Display All Tables")
                        print("Enter your choice: ")
                        val choice = readLine()?.toIntOrNull()

                        when (choice) {
                            1 -> {
                                println("Menu:")
                                menu.displayMenu()

                                print("Enter the item number to add (0 to cancel): ")
                                val itemNumber = readLine()?.toIntOrNull()

                                if (itemNumber != null && itemNumber != 0) {
                                    val item = menu.items.getOrNull(itemNumber?.minus(1) ?: -1)
                                    if (item != null) {
                                        table.addToOrder(item)
                                        println("${item.name} added to the order.")
                                    } else {
                                        println("Invalid item number.")
                                    }
                                } else {
                                    println("Add item cancelled.")
                                }
                            }
                            2 -> {
                                table.displayOrder()

                                print("Enter the item number to remove (0 to cancel): ")
                                val itemNumber = readLine()?.toIntOrNull()

                                if (itemNumber != null && itemNumber != 0) {
                                    val item = table.orders.getOrNull(itemNumber?.minus(1) ?: -1)
                                    if (item != null) {
                                        table.removeFromOrder(item)
                                        println("${item.name} removed from the order.")
                                    } else {
                                        println("Invalid item number.")
                                    }
                                } else {
                                    println("Remove item cancelled.")
                                }
                            }
                            3 -> break
                            else -> println("Invalid choice. Please try again.")
                        }
                    }
                } else {
                    println("Table $tableNumber does not exist.")
                }
            }
        }
    }

    // Methode, um die App zu starten und die Benutzerinteraktion zu ermöglichen
    fun startApp() {
        println("Welcome to the Cashier App!")

        while (true) {
            println("1. Open Table")
            println("2. Close Table")
            println("3. Display All Tables")
            println("4. Exit")
            print("Enter your choice: ")
            val choice = readLine()?.toIntOrNull()

            when (choice) {
                1 -> {
                    print("Enter the table number: ")
                    val tableNumber = readLine()?.toIntOrNull()
                    if (tableNumber != null) {
                        openTable(tableNumber)
                    } else {
                        println("Invalid table number.")
                    }
                }
                2 -> {
                    print("Enter the table number: ")
                    val tableNumber = readLine()?.toIntOrNull()
                    if (tableNumber != null) {
                        closeTable(tableNumber)
                    } else {
                        println("Invalid table number.")
                    }
                }
                3 -> displayAllTables()
                4 -> {
                    println("Thank you for using the Cashier App!")
                    exitProcess(0)
                }
                else -> println("Invalid choice. Please try again.")
            }
        }
    }
}

fun main() {
    // Erstellen des Menüs und Hinzufügen von Menüelementen
    val menu = Menu()
    menu.addItem(MenuItem("Water", 1.0))
    menu.addItem(MenuItem("Coffee", 2.5))
    menu.addItem(MenuItem("Cake", 3.0))

    // Erstellen des Kassierers und Starten der App
    val cashier = Cashier(menu)
    cashier.startApp()
}
