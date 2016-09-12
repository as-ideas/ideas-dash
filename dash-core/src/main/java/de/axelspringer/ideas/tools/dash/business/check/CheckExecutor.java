package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;

import java.util.List;


public interface CheckExecutor<T extends Check> {

    /**
     * @param check The check to execute
     * @return Never an empty collection. In case of "everything is fine" a "green" result check must be returned!
     */
    List<CheckResult> executeCheck(T check);

    boolean isApplicable(Check check);
}
