package de.axelspringer.ideas.tools.dash.business.check;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class CheckServiceTest {

	private final CheckService checkService = new CheckService();

	@Before
	public void injectExecutors() {

		final List<CheckExecutor> checkExecutors = new ArrayList<>();
		checkExecutors.add(new TestExecutorMatchingTestA());
		checkExecutors.add(new TestExecutorMatchingTestB());
		checkExecutors.add(new TestExecutorMatchingTestB());
		ReflectionTestUtils.setField(checkService, "checkExecutors", checkExecutors);
	}

	@Test
	public void testGetMatchingExecutor() {

		final CheckExecutor executor = checkService.executor(new TestCheckA());
		assertEquals(TestExecutorMatchingTestA.class, executor.getClass());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testExecutorCollectionNotChangedWhenGettingExecutor() {

		checkService.executor(new TestCheckA());
		final List<CheckExecutor> executors = (List<CheckExecutor>) ReflectionTestUtils.getField(checkService,
				"checkExecutors");
		assertEquals(3, executors.size());
	}

	@Test(expected = RuntimeException.class)
	public void testStuffExplodesOnTwoMatchingExecutors() {

		checkService.executor(new TestCheckB());
	}

	class TestCheckA extends Check {
		protected TestCheckA() {
			super(null, null, null);
		}
	}

	class TestCheckB extends Check {
		protected TestCheckB() {
			super(null, null, null);
		}
	}

	class TestExecutorMatchingTestA implements CheckExecutor {

		@Override
		public List<CheckResult> executeCheck(Check check) {
			return Collections.emptyList();
		}

		@Override
		public boolean isApplicable(Check check) {
			return check instanceof TestCheckA;
		}
	}

	class TestExecutorMatchingTestB implements CheckExecutor {

		@Override
		public List<CheckResult> executeCheck(Check check) {
			return Collections.emptyList();
		}

		@Override
		public boolean isApplicable(Check check) {
			return check instanceof TestCheckB;
		}
	}
}