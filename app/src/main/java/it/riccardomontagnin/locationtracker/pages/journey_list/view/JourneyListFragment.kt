package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.application.LocationTrackerApp
import it.riccardomontagnin.locationtracker.injector.CoreComponent
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.event.JourneyStatusChangedEvent
import it.riccardomontagnin.locationtracker.pages.journey_list.presenter.JourneyListPresenter
import it.riccardomontagnin.locationtracker.pages.journey_list.presenter.JourneyListView
import net.grandcentrix.thirtyinch.TiFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class JourneyListFragment: TiFragment<JourneyListPresenter, JourneyListView>(), JourneyListView {

    @Inject lateinit var presenter: JourneyListPresenter

    private lateinit var adapter: JourneysAdapter

    override fun providePresenter(): JourneyListPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerJourneyListComponent.builder()
                .coreComponent(LocationTrackerApp.getComponent(CoreComponent::class))
                .build().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_journey_list, container, false)

        adapter = JourneysAdapter()
        adapter.setOnJourneyClicked { presenter.journeyClicked(it) }

        val recyclerView = view.findViewById<RecyclerView>(R.id.journeys_recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true) fun journeyStatusChanged(event: JourneyStatusChangedEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        if (!event.journeyInProgress) presenter.updateList()
    }

    override fun setJourneys(journeys: List<JourneyData>) {
        adapter.setJourneys(journeys)
    }

    override fun showJourneyDetails(journey: JourneyData) {
        Toast.makeText(activity, "Show details", Toast.LENGTH_LONG).show()
    }
}