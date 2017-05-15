package com.example.android.testing.uiautomator.BasicSample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by liyiwei
 * on 2017/5/13.
 */
@RunWith(AndroidJUnit4.class)
public class BrowserTest {
    private static final String BASEC_PACKAGE_BROWSER = "com.android.browser";
    private static final String search_edittext_id = "url";
    private static final String gridview_id = "pagegrid";
    private UiDevice mDevice;

    @Before
    public void init() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        final String launcherPackageName = getLauncherPackageName();
        Assert.assertThat(launcherPackageName, CoreMatchers.notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), 5000);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASEC_PACKAGE_BROWSER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASEC_PACKAGE_BROWSER)), 5000);
    }

    @Test
    public void checkPreconditions() {
        if (mDevice == null) {
            throw new NullPointerException("-0-------------------------");
        }
        Matcher<Object> matcher = CoreMatchers.notNullValue();
        Assert.assertThat(mDevice, matcher);
    }

    /**
     * 测试自动搜索
     */
    @Test
    public void textChangeNewActivity() {
        BySelector selector = By.res(BASEC_PACKAGE_BROWSER, gridview_id);
        UiObject2 object1 = mDevice.findObject(selector);
        List<UiObject2> children = object1.getChildren();
        children.get(1).click();

        UiObject2 object2 = mDevice.wait(Until.findObject(By.res(BASEC_PACKAGE_BROWSER, search_edittext_id)), 5000);
        String text = object2.getText();
        Matcher<String> matcher = CoreMatchers.is(CoreMatchers.equalTo("百度一下"));//is "百度一下"
        Assert.assertThat(text, matcher);
    }

    private String getLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
