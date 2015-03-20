package de.axelspringer.ideas.tools.dash.business.check;

import lombok.Data;

@Data
public class Host {

    private String nameIp;

    public Host(String nameIp) {
        this.nameIp = nameIp;
    }
}
