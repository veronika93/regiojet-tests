package sk.veronika.simonova.regiojet.api

import sk.veronika.simonova.regiojet.api.data.Route
import java.util.Comparator

class RouteTravelTimeComparator: Comparator<Route> {

    override fun compare(o1: Route?, o2: Route?): Int {
        if(o1 == null || o2 == null || o1.travelTime == null ||o2.travelTime == null){
            return 0
        }
        val time1 = o1.travelTime.substring(0,2).toInt().times(60).plus(o1.travelTime.substring(3,5).toInt())
        val time2 = o2.travelTime.substring(0,2).toInt().times(60).plus(o2.travelTime.substring(3,5).toInt())
        return time1.compareTo(time2);
    }
}