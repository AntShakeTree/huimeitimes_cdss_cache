package com.hm.test;
import com.hm.Host;
import com.hm.cache.Cache;
import com.hm.cache.manager.HmCacheManager;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ant_shake_tree on 16/1/19.
 */
public class Testss {
    @Test
    public void testSss() throws IOException {
        long star=System.currentTimeMillis();
        Map<String,Long> list= Files.readAllLines(Paths.get("数据源.txt")).stream().map(line->line.substring(0,3)).collect(Collectors.groupingBy(s->s,Collectors.counting()));
        System.out.print(System.currentTimeMillis()-star);
    }
    @Test
    public void testCache() throws InterruptedException {
        Cache cache = HmCacheManager.ManagerDefault.get().getCache("xxxaa");
//       cache.put("1","123");
//       System.out.print(cache.get("1")+"");
//       cache.addList("xxx");
        Host h = new Host();
        h.setAddress("xxx");
        cache.addList(h);
        cache.addSet("xx");
        cache.addSet("xx1");
        cache.addSet("xx1");
        cache.addSet("xx2");
        cache.clear();
        cache.addSortSet("xx",1.0);
        cache.addSortSet("xx1",2.0);
        cache.addSortSet("xx2",3.0);
        cache.addSortSet("xx3",4.0);
        cache.addSortSet("xx3");

        Object key=cache.get("1");

        Set set = cache.set();

        Set set1 = cache.set();
    }


}
