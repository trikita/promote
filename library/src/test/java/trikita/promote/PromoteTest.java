package trikita.promote;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PromoteTest {

	@Mock
	Context c;

	@Before
	public void setUp() {
		SharedPreferences prefs = new SimplePreferences();
		when(c.getSharedPreferences("promote", 0)).thenReturn(prefs);
	}

	@Test
	public void testShowImmediate() {
		assertThat(Promote.after(0).times().show(c, 100, null), is(true));
		assertThat(Promote.after(0).days().show(c, 101, null), is(true));
		assertThat(Promote.after(0).days().after(0).times().show(c, 102, null), is(true));
	}

	@Test
	public void testAfter() {
		assertThat(Promote.after(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(2).times().show(c, 100, null), is(true));
	}

	@Test
	public void testAfterOnce() {
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(true));
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(false));
		assertThat(Promote.after(2).times().every(Integer.MAX_VALUE).times().show(c, 100, null), is(false));
	}

	@Test
	public void testEvery() {
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2).times().show(c, 100, null), is(false));
	}

	@Test
	public void testEveryChanged() {
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.every(3).times().show(c, 100, null), is(false));
		assertThat(Promote.every(3).times().show(c, 100, null), is(false));
		assertThat(Promote.every(3).times().show(c, 100, null), is(true));
		assertThat(Promote.every(3).times().show(c, 100, null), is(false));
		assertThat(Promote.every(3).times().show(c, 100, null), is(false));
		assertThat(Promote.every(3).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2).times().show(c, 100, null), is(true));
	}

	@Test
	public void testEveryBackoff() {
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(true));
		assertThat(Promote.every(2, 2f).times().show(c, 100, null), is(false));
	}

	@Test
	public void testAfterEvery() {
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(3).times().every(2).times().show(c, 100, null), is(false));
	}

	@Test
	public void testDaysNow() {
		assertThat(Promote.after(0).days().show(c, 100, null), is(true));
		assertThat(Promote.after(0).days().show(c, 100, null), is(true));
		assertThat(Promote.after(0).days().show(c, 100, null), is(true));
		assertThat(Promote.after(0).days().every(2).times().show(c, 100, null), is(false));
		assertThat(Promote.after(0).days().every(2).times().show(c, 100, null), is(true));
		assertThat(Promote.after(0).days().every(2).times().show(c, 100, null), is(false));
	}

	@Test
	public void testBan() {
		assertThat(Promote.after(1).times().show(c, 100, null), is(false));
		assertThat(Promote.after(1).times().show(c, 100, null), is(true));
		assertThat(Promote.after(1).times().show(c, 100, null), is(true));
		Promote.ban(c, 100);
		assertThat(Promote.after(1).times().show(c, 100, null), is(false));
		assertThat(Promote.after(1).times().show(c, 100, null), is(false));
	}

	@Test
	public void testReset() {
		assertThat(Promote.after(1).times().show(c, 100, null), is(false));
		assertThat(Promote.after(1).times().show(c, 100, null), is(true));
		assertThat(Promote.after(1).times().show(c, 100, null), is(true));
		Promote.reset(c);
		assertThat(Promote.after(1).times().show(c, 100, null), is(false));
		assertThat(Promote.after(1).times().show(c, 100, null), is(true));
	}
}
