package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.application.LocationTrackerApp
import it.riccardomontagnin.locationtracker.injector.CoreComponent
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.event.JourneyStatusChangedEvent
import it.riccardomontagnin.locationtracker.pages.journey_details.view.JourneyDetailsActivity
import it.riccardomontagnin.locationtracker.pages.journey_list.presenter.JourneyListPresenter
import it.riccardomontagnin.locationtracker.pages.journey_list.presenter.JourneyListView
import net.grandcentrix.thirtyinch.TiFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

/**
 * Fragment representing the implementation of [JourneyListView].
 * @see JourneyListView
 */
class JourneyListFragment: TiFragment<JourneyListPresenter, JourneyListView>(), JourneyListView {

    @Inject lateinit var presenter: JourneyListPresenter

    private lateinit var adapter: JourneysAdapter

    /**
     * @inheritDoc
     */
    override fun providePresenter(): JourneyListPresenter = presenter

    /**
     * @inheritDoc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerJourneyListComponent.builder()
                .coreComponent(LocationTrackerApp.getComponent(CoreComponent::class))
                .build().inject(this)
        super.onCreate(savedInstanceState)
    }

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    /**
     * @inheritDoc
     */
    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Listens for all the [JourneyStatusChangedEvent] that might be emitted, catching also the ones
     * that have been emitted before the listening started.
     * @param event: Object representing the most recent event emitted.
     */
    @Subscribe(sticky = true)
    internal fun journeyStatusChanged(event: JourneyStatusChangedEvent) {
        // Remove the sticky event in order to not catch it again the next time
        EventBus.getDefault().removeStickyEvent(event)

        // If the event tells us that the journey is not in progress than update the list
        if (!event.journeyInProgress) presenter.updateList()

    }

    /**
     * @inheritDoc
     */
    override fun setJourneys(journeys: List<JourneyData>) {
        adapter.setJourneys(journeys)
    }

    /**
     * @inheritDoc
     */
    override fun showJourneyDetails(journey: JourneyData) {
        JourneyDetailsActivity.start(activity!!, journey)
    }

}