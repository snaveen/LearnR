"""Search interface for DeathCluster data"""
# coding=utf-8
__author__ = 'Sriram Velamur'

#pylint:disable=E1103
# Disables
# E1103 : Non inferred firstChild member from minidom

import sys
sys.dont_write_bytecode = True
import requests
from xml.dom import minidom
import re


class Search(object):
    """Search class"""

    def __init__(self, query=None, search_url=None):
        """
        Search class init.
        :param query: str|unicode. Base query string to bootstrap class
        @todo: Config dict for class init to specify url source, meta.
        """
        if not isinstance(query, (str, unicode, list, tuple)):
            raise Exception("No query. Exiting")

        # Google search feeds url
        search_url = search_url or \
            "https://news.google.com/news/feeds?output=rss&q="

        # Set up a local dict and populate values from locals
        fields = "query*data*search_url*matcher".split("*")
        fields = {x: locals().get(x, None) for x in fields}

        # Set class attributes from local dict
        for field in fields.iterkeys():
            setattr(self, field, locals().get(field, None))

        # Legacy. Works with link element inside result entity
        # matcher = re.compile('.*?url=(.*?)\?.*?')

        # Current. Works with the guid element in result entity
        self.url_matcher = re.compile(
            'tag:news.google.com,\d{4}:cluster=(.*)\??.*')

        self.desc_matcher = re.compile(
            '<div class="lh">.*?<b>(.*?)<a class="p".*')

        self.items = None

    def get_url(self, url):
        """Regex filter to extract actual url to article"""
        if not isinstance(url, (str, unicode)):
            raise Exception("Cannot proceed. No stringy url")
        matches = re.findall(self.url_matcher, url)
        return matches[0] if len(matches) == 1 else None

    def get_desc(self, desc):
        """Get search article description"""
        if not isinstance(desc, (str, unicode)):
            raise Exception("Cannot proceed. No stringy description")
        matches = re.findall(self.desc_matcher, desc)
        matches = matches[0] if len(matches) == 1 else None
        esc_rep = {
            '&nbsp': '&',
            '&quot': "'"
        }
        if matches:
            matches = re.sub("(<.*?>)", " ", matches)
            for (key, val) in esc_rep.iteritems():
                matches = re.sub(key, val, matches)
            matches = matches.strip()

        return matches

    def load_xml(self):
        """
        Work with the response XML and load search results
        @todo: Custom config as an option to work with multiple feeds.
        """
        data = getattr(self, "data")
        tree = minidom.parseString(data)
        items = tree.firstChild.firstChild.getElementsByTagName('item')
        item_fields = ["pubDate", "guid", "description"]
        items_data = {item_x.getElementsByTagName(
            'title')[0].firstChild.wholeText: {
            x: item_x.getElementsByTagName(x)[0].firstChild.wholeText
            for x in item_fields if len(
                item_x.getElementsByTagName(x)) == 1} for item_x in items}

        # Data key customizations
        for (key, item) in items_data.iteritems():
            plink = self.get_url(item["guid"])
            item["url"] = plink
            item.pop("guid")
            pdesc = self.get_desc(item["description"])
            item["description"] = pdesc
            item["source"] = key.split("-")[1].strip() if\
                                len(key.split("-")) >= 2 else None
            item["title"] = key.split("-")[0].strip() if \
                                len(key.split("-")) >= 2 else None
            item["description"] = item["description"].replace(key, "")
            item["pub_date"] = item["pubDate"]
            item.pop("pubDate")

        setattr(self, "items", items_data)

    def _fetch_data(self):
        """
        Syntactic sugar.
        Fetches data with requests and sets up the result.
        """
        setattr(self, "data", getattr(requests.get(
            getattr(self, "curr_url")), "_content"))
        self.load_xml()
        return getattr(self, "items")

    def search(self, filter_qs=None):
        """
        Search instance method.
        :param filter: str|unicode Provide extra filter strings for search
        """
        field = getattr(self, "query")
        if filter_qs:
            if isinstance(filter_qs, (str, unicode)):
                field += " + " + filter_qs
            elif isinstance(filter_qs, (list, tuple)):
                pass  # todo: Work on multiple queries and aggregate uniques
            else:
                # Pass silently/stringify ?
                raise Exception("Non stringy filter")

        if not getattr(self, "search_url"):
            raise Exception("No search url set")

        if isinstance(field, (str, unicode)):
            setattr(self, "curr_url", "%s%s" %
                    (getattr(self, "search_url"), field))
            return self._fetch_data()
        elif isinstance(field, (list, tuple)):
            results = {}
            for query_filter in field:
                setattr(self, "curr_url", "%s%s" %
                        (getattr("search_url"), field))
                field += " + " + query_filter
                results.update(self._fetch_data())
                return results


if __name__ == '__main__':
    try:
        SEARCH_INST = Search("deaths")
        SEARCH_INST.search(filter_qs="more than")
        S_RESP = str(SEARCH_INST.items)
        S_RESP = S_RESP.replace("u'", '"').replace("',", '"')
        S_RESP = S_RESP.replace("'}", '"')
        with open("../data_store/py_data.json", "a") as data_file:
            data_file.write(S_RESP)
    except KeyboardInterrupt:
        quit("")
