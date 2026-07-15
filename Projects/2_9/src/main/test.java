package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import orms.linelistEntity;
import orms.productEntity;
import orms.sub_areaEntity;
import orms.userEntity;

public class test {

	static List<Integer> visited = new ArrayList<>();
	static List<Integer> list = new ArrayList<>();
	static Map<Integer, List<int[]>> map = new HashMap<>(); // sno -> [neighborSno, weight] 목록
	static Map<Integer, Point> pointMap = new HashMap<>();   // sno -> (sx, sy), 거리 계산용

	public static void main(String[] args) {
	    linelistEntity.findAll().forEach(c -> {
	        sub_areaEntity s1 = sub_areaEntity.findById(c.u).get();
	        sub_areaEntity s2 = sub_areaEntity.findById(c.v).get();
	        int dist = (int) Math.sqrt(Math.pow(s1.sx - s2.sx, 2) + Math.pow(s1.sy - s2.sy, 2));

	        map.computeIfAbsent(c.u, k -> new ArrayList<>()).add(new int[]{c.v, dist});
	        map.computeIfAbsent(c.v, k -> new ArrayList<>()).add(new int[]{c.u, dist});
	    });

	    System.out.println(dijkstra(userEntity.findById(1).get().sno, productEntity.findById(1).get().sno));
	}

	static List<Integer> dijkstra(int start, int end) {
	    Map<Integer, Integer> dist = new HashMap<>();     // 지금까지 알려진 최단 거리
	    Map<Integer, Integer> parent = new HashMap<>();
	    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // [노드, 거리] 중 거리 오름차순

	    dist.put(start, 0);
	    pq.add(new int[]{start, 0});

	    while (!pq.isEmpty()) {
	        int[] cur = pq.poll();
	        int p = cur[0], d = cur[1];

	        if (d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue; // 이미 더 짧은 걸로 처리된 노드면 skip
	        if (p == end) break;

	        for (int[] edge : map.getOrDefault(p, new ArrayList<>())) {
	            int next = edge[0], weight = edge[1];
	            int newDist = d + weight;
	            if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
	                dist.put(next, newDist);
	                parent.put(next, p);
	                pq.add(new int[]{next, newDist});
	            }
	        }
	    }

	    if (!dist.containsKey(end)) return new ArrayList<>(); // 경로 없음

	    LinkedList<Integer> path = new LinkedList<>();
	    Integer cur = end;
	    while (cur != null) {
	        path.addFirst(cur);
	        cur = (cur == start) ? null : parent.get(cur);
	    }
	    return path;
	}
}
