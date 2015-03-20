package de.axelspringer.ideas.tools.dash.business.check;

import java.util.List;


public interface CheckExecutor {

    /**
     * @param check
     * @return Never an empty collection. In case of "everything is fine" a "green" result check must be returned!
     */
    List<CheckResult> executeCheck(Check check);

    boolean isApplicable(Check check);
}
