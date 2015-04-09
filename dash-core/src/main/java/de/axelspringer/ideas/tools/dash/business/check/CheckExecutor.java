package de.axelspringer.ideas.tools.dash.business.check;

import java.util.List;


public interface CheckExecutor<T extends Check> {

    /**
     * @param check
     * @return Never an empty collection. In case of "everything is fine" a "green" result check must be returned!
     */
    List<CheckResult> executeCheck(T check);

    boolean isApplicable(Check check);
}
