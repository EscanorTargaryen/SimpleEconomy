package dev.escanortargaryen;

import net.milkbowl.vault.economy.Economy;

public class VaultInjector extends VaultEconomy implements Economy {

    public VaultInjector(VaultEconomy eco) {
        super(eco.table);
    }

}
