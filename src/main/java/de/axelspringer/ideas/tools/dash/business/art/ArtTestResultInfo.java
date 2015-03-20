package de.axelspringer.ideas.tools.dash.business.art;

import lombok.Data;

/**
 * @author Timo Ulich
 */
@Data
public class ArtTestResultInfo {

    private String status;

    public boolean isSuccessfull() {
        return "success".equalsIgnoreCase(status);
    }
}
